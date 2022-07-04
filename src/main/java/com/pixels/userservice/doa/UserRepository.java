package com.pixels.userservice.doa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pixels.userservice.model.PixelSenseUser;

@Repository
public interface UserRepository extends CrudRepository<PixelSenseUser, String> {
	@org.springframework.data.jpa.repository.Query("select new #{#entityName}(u.username, u.password) from #{#entityName} u where u.username = ?1")
	public PixelSenseUser findForAuth(String username);
}
