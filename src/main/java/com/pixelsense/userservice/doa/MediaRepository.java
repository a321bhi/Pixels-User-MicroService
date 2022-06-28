package com.pixelsense.userservice.doa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pixelsense.userservice.model.Media;
import com.pixelsense.userservice.model.PixelSenseUser;

@Repository
public interface MediaRepository extends CrudRepository<Media, String>  {
	@org.springframework.data.jpa.repository.Query("select u.mediaId from #{#entityName} u where u.mediaPostedBy = ?1")
	public List<String> findAllMediaOfUser(PixelSenseUser user);
	
//	@org.springframework.data.jpa.repository.Query("select v.userId from #{#entityName} u join u.likedBy v where u.mediaId = ?1")
//	public List<String> findUsersWhoLikedThisMedia(Media media);
}
