package com.keep.changes.address;

import java.util.HashSet;
import java.util.Set;

import com.keep.changes.fundraiser.Fundraiser;
import com.keep.changes.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "varchar(100)", nullable = false)
	private String area;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	private String city;

	@Column(columnDefinition = "varchar(50)", nullable = false)
	private String state;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	private String country;

	@Column(columnDefinition = "varchar(20)", nullable = false)
	private String pincode;

	@OneToMany(mappedBy = "address")
	private Set<User> associatedUser = new HashSet<>();

	@OneToMany(mappedBy = "address")
	private Set<Fundraiser> associatedFundraiser = new HashSet<>();

}
