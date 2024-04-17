package com.keep.changes.category;

import java.util.HashSet;
import java.util.Set;

import com.keep.changes.fundraiser.Fundraiser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(columnDefinition = "varchar(30)", nullable = false, unique = true)
	private String categoryName;

	@Column(columnDefinition = "longText", nullable = false)
	private String categoryDescription;

	@Column(columnDefinition = "varchar(100)", nullable = false)
	private String categorySvg;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Fundraiser> fundraisers = new HashSet<>();

}
