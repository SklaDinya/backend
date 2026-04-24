package skladinya.persistence.redis.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.UUID;

@Data
@RedisHash("Version")
@NoArgsConstructor
@AllArgsConstructor
public class VersionModel {

    @Id
    private UUID userId;

    private String version;

    @TimeToLive
    private int ttl;
}
