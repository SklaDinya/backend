package skladinya.persistence.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.cell.CellSearchOptions;
import skladinya.domain.repositories.CellRepository;
import skladinya.persistence.entities.CellEntity;
import skladinya.persistence.entities.StorageEntity;
import skladinya.persistence.mappers.CellMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CellRepositoryPostgres implements CellRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Cell create(Cell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Cell cannot be null");
        }

        StorageEntity storage = em.find(StorageEntity.class, cell.storageId());

        CellEntity entity = CellMapper.toEntity(cell, storage);

        em.persist(entity);
        em.flush();

        return CellMapper.toDomain(entity);
    }

    @Override
    public List<Cell> getAllBySearchOptions(UUID storageId, CellSearchOptions options) {
        if (storageId == null) return List.of();

        StringBuilder sb = new StringBuilder("SELECT c FROM CellEntity c WHERE c.storage.id = :storageId");

        if (options.cellClasses() != null && !options.cellClasses().isEmpty()) {
            sb.append(" AND c.cellClass IN :cellClasses");
        }

        if (options.startBooking() != null && options.timeBooking() != null) {
            LocalDateTime endBooking = options.startBooking().plus(options.timeBooking());
            sb.append(" AND NOT EXISTS (")
                    .append("SELECT 1 FROM BookingEntity b JOIN b.cells bc ")
                    .append("WHERE bc = c AND b.startTime < :endBooking AND (b.startTime + b.bookingTimeHour * 1 hour) > :startBooking")
                    .append(")");
        }

        sb.append(" ORDER BY c.name ASC");

        var query = em.createQuery(sb.toString(), CellEntity.class);
        query.setParameter("storageId", storageId);

        if (options.cellClasses() != null && !options.cellClasses().isEmpty()) {
            query.setParameter("cellClasses", options.cellClasses());
        }

        if (options.startBooking() != null && options.timeBooking() != null) {
            LocalDateTime endBooking = options.startBooking().plus(options.timeBooking());
            query.setParameter("startBooking", options.startBooking());
            query.setParameter("endBooking", endBooking);
        }

        query.setFirstResult(options.pageNumber() * options.pageSize());
        query.setMaxResults(options.pageSize());

        return query.getResultStream()
                .map(CellMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cell> getAllByCellIds(List<UUID> cellIds) {
        if (cellIds == null || cellIds.isEmpty()) return List.of();

        var query = em.createQuery("SELECT c FROM CellEntity c WHERE c.id IN :ids", CellEntity.class);
        query.setParameter("ids", cellIds);

        return query.getResultStream()
                .map(CellMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCellClasses(UUID storageId) {
        if (storageId == null) return List.of();

        var query = em.createQuery(
                "SELECT DISTINCT c.cellClass FROM CellEntity c WHERE c.storage.id = :storageId",
                String.class
        );
        query.setParameter("storageId", storageId);

        return query.getResultList();
    }
}
