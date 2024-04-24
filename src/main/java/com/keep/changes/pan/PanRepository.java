package com.keep.changes.pan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.keep.changes.user.User;

@Repository
public interface PanRepository extends JpaRepository<Pan, Long> {

	Optional<Pan> findByPanNumber(String panNumber);

	List<Pan> findByNameOnPanContaining(String nameOnPan);

	Optional<Pan> findByPanHolder(User panHolder);

}
