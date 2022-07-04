package com.pixels.userservice.doa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pixels.userservice.model.MediaComment;

@Repository
public interface MediaCommentRepository extends CrudRepository<MediaComment, String> {

}
