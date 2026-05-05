package skladinya.tests.unit.services.cell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import skladinya.domain.exceptions.SklaDinyaException;
import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.cell.CellCreate;
import skladinya.domain.models.cell.CellSearchOptions;
import skladinya.domain.repositories.CellRepository;
import skladinya.domain.services.CellService;
import skladinya.domain.services.StorageService;
import skladinya.services.cell.CellServiceImpl;
import skladinya.tests.helper.builder.CellBuilder;
import skladinya.tests.helper.builder.StorageBuilder;
import skladinya.tests.helper.synchronizer.SynchronizerTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CellServiceTest {

    @Mock
    private CellRepository cellRepository;

    @Mock
    private StorageService storageService;

    private CellService cellService;

    @BeforeEach
    void setUp() {
        var synchronizer = new SynchronizerTest();
        cellService = new CellServiceImpl(cellRepository, storageService, synchronizer);
    }

    @Test
    void givenValidData_whenCreate_thenReturnCell() {
        var storage = StorageBuilder.builder().build();
        var cell = CellBuilder.builder().storageId(storage.storageId()).build();
        given(storageService.getByStorageId(storage.storageId())).willReturn(storage);
        given(cellRepository.getByName(storage.storageId(), cell.name())).willReturn(Optional.empty());
        given(cellRepository.create(any())).willReturn(cell);
        var form = new CellCreate(cell.name(), cell.cellClass());

        var result = cellService.create(storage.storageId(), form);

        assertEquals(cell, result);
    }

    @Test
    void givenInvalidStorageId_whenCreate_thenThrowException() {
        var storageId = UUID.randomUUID();
        willThrow(SklaDinyaException.notFound("")).given(storageService).getByStorageId(storageId);
        var form = new CellCreate("6", "7");

        assertThrows(SklaDinyaException.class, () -> cellService.create(storageId, form));

        verify(cellRepository, never()).getByName(any(), any());
        verify(cellRepository, never()).create(any());
    }

    @Test
    void givenExistsName_whenCreate_thenThrowException() {
        var storage = StorageBuilder.builder().build();
        var cell = CellBuilder.builder().storageId(storage.storageId()).build();
        var other = CellBuilder.builder().storageId(storage.storageId()).name(cell.name()).build();
        given(storageService.getByStorageId(storage.storageId())).willReturn(storage);
        given(cellRepository.getByName(storage.storageId(), cell.name())).willReturn(Optional.of(other));
        var form = new CellCreate(cell.name(), cell.cellClass());

        assertThrows(SklaDinyaException.class, () -> cellService.create(storage.storageId(), form));

        verify(cellRepository, never()).create(any());
    }

    @Test
    void givenValidData_whenGetAllBySearchOptions_thenReturnCells() {
        var storage = StorageBuilder.builder().build();
        var cell1 = CellBuilder.builder().storageId(storage.storageId()).build();
        var cell2 = CellBuilder.builder().storageId(storage.storageId()).build();
        var list = List.of(cell1, cell2);
        var options = new CellSearchOptions(null, null, List.of(), 50, 0);
        given(storageService.getByStorageId(storage.storageId())).willReturn(storage);
        given(cellRepository.getAllBySearchOptions(storage.storageId(), options)).willReturn(list);

        var result = cellService.getAllBySearchOptions(storage.storageId(), options);

        assertEquals(list.size(), result.size());
        for (var i = 0; i < list.size(); ++i) {
            assertEquals(list.get(i), result.get(i));
        }
    }

    @Test
    void givenNoCells_whenGetAllBySearchOptions_thenReturnEmptyList() {
        var storage = StorageBuilder.builder().build();
        var options = new CellSearchOptions(null, null, List.of(), 50, 0);
        given(storageService.getByStorageId(storage.storageId())).willReturn(storage);
        given(cellRepository.getAllBySearchOptions(storage.storageId(), options)).willReturn(List.of());

        var result = cellService.getAllBySearchOptions(storage.storageId(), options);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenInvalidStorageId_whenGetAllBySearchOptions_thenThrowException() {
        var storageId = UUID.randomUUID();
        var options = new CellSearchOptions(null, null, List.of(), 50, 0);
        willThrow(SklaDinyaException.notFound("")).given(storageService).getByStorageId(storageId);

        assertThrows(SklaDinyaException.class, () -> cellService.getAllBySearchOptions(storageId, options));

        verify(cellRepository, never()).getAllBySearchOptions(any(), any());
    }

    @Test
    void givenValidData_whenGetAllByCellIds_thenReturnCells() {
        var storage = StorageBuilder.builder().build();
        var cell1 = CellBuilder.builder().storageId(storage.storageId()).build();
        var cell2 = CellBuilder.builder().storageId(storage.storageId()).build();
        var list = List.of(cell1, cell2);
        var ids = list.stream().map(Cell::cellId).toList();
        given(storageService.getByStorageId(storage.storageId())).willReturn(storage);
        given(cellRepository.getAllByCellIds(ids)).willReturn(list);

        var result = cellService.getAllByCellIds(storage.storageId(), ids);

        assertEquals(list.size(), result.size());
        for (var i = 0; i < list.size(); ++i) {
            assertEquals(list.get(i), result.get(i));
        }
    }

    @Test
    void givenNoCells_whenGetAllByCellIds_thenReturnEmptyList() {
        var storage = StorageBuilder.builder().build();
        var ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        given(storageService.getByStorageId(storage.storageId())).willReturn(storage);
        given(cellRepository.getAllByCellIds(ids)).willReturn(List.of());

        var result = cellService.getAllByCellIds(storage.storageId(), ids);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenInvalidStorageId_whenGetAllByCellIds_thenThrowException() {
        var storageId = UUID.randomUUID();
        var ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        willThrow(SklaDinyaException.notFound("")).given(storageService).getByStorageId(storageId);

        assertThrows(SklaDinyaException.class, () -> cellService.getAllByCellIds(storageId, ids));

        verify(cellRepository, never()).getAllByCellIds(any());
    }

    @Test
    void givenValidData_whenGetAllCellClasses_thenReturnCells() {
        var storage = StorageBuilder.builder().build();
        var list = List.of("A", "B", "C");
        given(storageService.getByStorageId(storage.storageId())).willReturn(storage);
        given(cellRepository.getAllCellClasses(storage.storageId())).willReturn(list);

        var result = cellService.getAllCellClasses(storage.storageId());

        assertEquals(list.size(), result.size());
        for (var i = 0; i < list.size(); ++i) {
            assertEquals(list.get(i), result.get(i));
        }
    }

    @Test
    void givenNoCells_whenGetAllCellClasses_thenReturnEmptyList() {
        var storage = StorageBuilder.builder().build();
        given(storageService.getByStorageId(storage.storageId())).willReturn(storage);
        given(cellRepository.getAllCellClasses(storage.storageId())).willReturn(List.of());

        var result = cellService.getAllCellClasses(storage.storageId());

        assertTrue(result.isEmpty());
    }

    @Test
    void givenInvalidStorageId_whenGetAllCellClasses_thenThrowException() {
        var storageId = UUID.randomUUID();
        willThrow(SklaDinyaException.notFound("")).given(storageService).getByStorageId(storageId);

        assertThrows(SklaDinyaException.class, () -> cellService.getAllCellClasses(storageId));

        verify(cellRepository, never()).getAllCellClasses(any());
    }
}
