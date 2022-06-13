package com.pixelsense.pixelsense.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pixelsense.pixelsense.model.PixelSenseUser;

@Repository
public interface UserRepository extends CrudRepository<PixelSenseUser, String>{

}
