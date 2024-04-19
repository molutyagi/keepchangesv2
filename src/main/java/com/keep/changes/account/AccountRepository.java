package com.keep.changes.account;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.keep.changes.user.User;
import com.keep.changes.fundraiser.Fundraiser;

public interface AccountRepository extends JpaRepository<Account, Long> {

	List<Account> findByHoldingEntity(User holdingEntity);

	Account findByAssociatedFundraisers(Fundraiser associatedFundraiser);

}
