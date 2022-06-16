package com.pixelsense.userservice.doa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pixelsense.userservice.model.PixelSenseUser;

@Repository
public interface UserRepository extends CrudRepository<PixelSenseUser, String>{

}
