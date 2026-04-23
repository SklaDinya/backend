package skladinya.persistence.postgres.repositories;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import skladinya.domain.models.cell.Cell;
import skladinya.domain.models.cell.CellSearchOptions;
import skladinya.domain.repositories.CellRepository;
import skladinya.persistence.postgres.entities.BookingEntity;
import skladinya.persistence.postgres.entities.CellEntity;
import skladinya.persistence.postgres.mappers.CellMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

interface SpringCellRepository extends JpaRepository<CellEntity, UUID>, JpaSpecificationExecutor<CellEntity> {

    List<CellEntity> findAllByIdInOrderByName(List<UUID> ids);

    @Query("select distinct c.cellClass from CellEntity c where c.storageId = :storageId")
    List<String> findAllCellClasses(UUID storageId);
}

class CellSpecification {

    public static Specification<CellEntity> byOptions(UUID storageId, CellSearchOptions options) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("storageId"), storageId));

            if (options.cellClasses() != null && !options.cellClasses().isEmpty()) {
                predicates.add(root.get("cellClass").in(options.cellClasses()));
            }

            if (options.startBooking() != null && options.timeBooking() != null) {

                LocalDateTime endBooking = options.startBooking().plus(options.timeBooking());

                Subquery<UUID> subquery = query.subquery(UUID.class);
                Root<BookingEntity> bookingRoot = subquery.from(BookingEntity.class);
                Join<BookingEntity, CellEntity> cellJoin = bookingRoot.join("cells");

                subquery.select(bookingRoot.get("id"));

                Predicate sameCell = cb.equal(cellJoin.get("id"), root.get("id"));

                Predicate overlap = cb.and(
                        cb.lessThan(bookingRoot.get("startTime"), endBooking),
                        cb.greaterThan(bookingRoot.get("endTime"), options.startBooking())
                );

                subquery.where(cb.and(sameCell, overlap));

                predicates.add(cb.not(cb.exists(subquery)));
            }

            query.distinct(true);

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}

@Repository
@RequiredArgsConstructor
public class PostgresCellRepository implements CellRepository {

    private final SpringCellRepository repo;

    @Override
    public Cell create(Cell cell) {
        CellEntity entity = CellMapper.toEntity(cell);
        CellEntity saved = repo.save(entity);
        return CellMapper.toDomain(saved);
    }

    @Override
    public List<Cell> getAllBySearchOptions(UUID storageId, CellSearchOptions options) {
        Pageable pageable = PageRequest.of(options.pageNumber(), options.pageSize());

        return repo.findAll(CellSpecification.byOptions(storageId, options), pageable)
                .stream()
                .map(CellMapper::toDomain)
                .toList();
    }

    @Override
    public List<Cell> getAllByCellIds(List<UUID> cellIds) {
        if (cellIds == null || cellIds.isEmpty()) return List.of();

        return repo.findAllByIdInOrderByName(cellIds).stream()
                .map(CellMapper::toDomain)
                .toList();
    }

    @Override
    public List<String> getAllCellClasses(UUID storageId) {
        return repo.findAllCellClasses(storageId);
    }
}
