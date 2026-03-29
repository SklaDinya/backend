package skladinya.domain.services;

import skladinya.domain.models.storage.Storage;

public interface EmailService {

    void sendStorageCreated(String email, Storage storage);

    void sendStorageApproved(String email, Storage storage);

    void sendStorageRejected(String email, Storage storage);
}
