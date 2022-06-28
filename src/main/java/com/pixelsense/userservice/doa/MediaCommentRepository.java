package com.pixelsense.userservice.doa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pixelsense.userservice.model.MediaComment;

@Repository
public interface MediaCommentRepository extends CrudRepository<MediaComment, String> {

}
