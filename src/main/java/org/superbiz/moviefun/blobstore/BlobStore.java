package org.superbiz.moviefun.blobstore;

import java.util.Optional;

public interface BlobStore {

    void put(Blob blob) throws Exception;

    Optional<Blob> get(String name) throws Exception;

    void deleteAll();

}
