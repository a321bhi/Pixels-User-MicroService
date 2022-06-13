package com.pixelsense.pixelsense.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pixelsense.pixelsense.model.Media;

@Repository
public interface PostRepository extends MongoRepository<Media,String>{

}
