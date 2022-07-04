package com.pixels.userservice.doa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pixels.userservice.model.Media;
import com.pixels.userservice.model.PixelSenseUser;

@Repository
public interface MediaRepository extends CrudRepository<Media, String>  {
	@org.springframework.data.jpa.repository.Query("select u.mediaId from #{#entityName} u where u.mediaPostedBy = ?1")
	public List<String> findAllMediaOfUser(PixelSenseUser user);

}
