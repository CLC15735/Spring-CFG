package com.qa.demo.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.qa.demo.persistence.domain.Flower;
import com.qa.demo.persistence.repo.FlowerRepo;
import com.qa.demo.service.FlowerService;

@RunWith(MockitoJUnitRunner.class)
public class FlowerServiceUnitTest {

	private final Flower FLOWER = new Flower("orchid", 27, "white", 44.94, false, "verdecora");

	private Flower savedFlower;

	@Mock
	private FlowerRepo repo;

	@InjectMocks
	private FlowerService service;

	@Before
	public void init() {
		this.savedFlower = new Flower(FLOWER.getType(), FLOWER.getHeight(), FLOWER.getColour(), FLOWER.getPrice(),
				FLOWER.isPoisonous(), FLOWER.getSeller());
		this.savedFlower.setId(1L);
	}

	@Test
	public void testCreate() {
		when(this.repo.save(FLOWER)).thenReturn(savedFlower);

		assertEquals(savedFlower, service.create(FLOWER));

		verify(this.repo, Mockito.times(1)).save(FLOWER);
	}

	@Test
	public void testUpdate() {
		Mockito.when(this.repo.findById(savedFlower.getId())).thenReturn(Optional.of(savedFlower));

		Flower newFlower = new Flower("piers", 5, "white", 0, true, "FlowerCo");
		Flower newFlowerWthID = new Flower(newFlower.getType(), newFlower.getHeight(), newFlower.getColour(), newFlower.getPrice(), newFlower.isPoisonous(), newFlower.getSeller());
		newFlowerWthID.setId(savedFlower.getId());

		Mockito.when(this.repo.save(newFlowerWthID)).thenReturn(newFlowerWthID);

		assertEquals(newFlowerWthID, this.service.update(newFlower, savedFlower.getId()));

		Mockito.verify(this.repo, Mockito.times(1)).findById(savedFlower.getId());
		Mockito.verify(this.repo, Mockito.times(1)).save(newFlowerWthID);
	}

	@Test
	public void testDeleteFails() {
		final long ID = 1L;
		final boolean RESULT = true;
		Mockito.when(this.repo.existsById(ID)).thenReturn(RESULT);

		assertEquals(RESULT, this.service.delete(ID));

		Mockito.verify(this.repo, Mockito.times(1)).existsById(ID);

	}

	@Test
	public void testDeleteSucceeds() {
		final long ID = 1L;
		final boolean RESULT = false;
		Mockito.when(this.repo.existsById(ID)).thenReturn(RESULT);

		assertEquals(RESULT, this.service.delete(ID));

		Mockito.verify(this.repo, Mockito.times(1)).existsById(ID);
	}

}
