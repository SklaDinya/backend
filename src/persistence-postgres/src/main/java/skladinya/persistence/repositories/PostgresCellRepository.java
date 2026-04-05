package skladinya.persistence.repositories;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
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
import skladinya.persistence.entities.BookingEntity;
import skladinya.persistence.entities.CellEntity;
import skladinya.persistence.mappers.CellMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

interface SpringCellRepository extends JpaRepository<CellEntity, UUID>, JpaSpecificationExecutor<CellEntity> {

    List<CellEntity> findAllByIdIn(List<UUID> ids);

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

                LocalDateTime end = options.startBooking().plus(options.timeBooking());

                Join<CellEntity, BookingEntity> bookingJoin = root.join("bookings", JoinType.LEFT);

                Predicate noBookings = cb.isNull(bookingJoin.get("id"));

                predicates.add(noBookings);
            }

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
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

        return repo.findAllByIdIn(cellIds).stream()
                .map(CellMapper::toDomain)
                .toList();
    }

    @Override
    public List<String> getAllCellClasses(UUID storageId) {
        return repo.findAllCellClasses(storageId);
    }
}
