package org.molgenis.data.support;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.molgenis.data.Entity;
import org.molgenis.data.EntityMetaData;
import org.molgenis.data.Query;
import org.molgenis.data.RepositoryCapability;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

import junit.framework.Assert;

public class AbstractRepositoryTest
{
	private AbstractRepository abstractRepository;
	private DefaultEntityMetaData entityMetaData;

	@BeforeTest
	public void beforeTest()
	{
		entityMetaData = new DefaultEntityMetaData("entity");
		entityMetaData.addAttribute("id").setIdAttribute(true);
		abstractRepository = Mockito.spy(new AbstractRepository()
		{

			@Override
			public Iterator<Entity> iterator()
			{
				return null;
			}

			@Override
			public EntityMetaData getEntityMetaData()
			{
				return entityMetaData;
			}

			@Override
			public Set<RepositoryCapability> getCapabilities()
			{
				return Collections.emptySet();
			}
		});
	}

	@BeforeMethod
	public void beforeMethod()
	{
		Mockito.reset(abstractRepository);
	}

	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void testFindAllIdsForRepositoryThatDoesntHaveFindAll()
	{
		Iterable<Entity> result = abstractRepository.findAll(newArrayList("1", "2", "3"));
		assertEquals(newArrayList(), newArrayList(result));
	}

	@Test
	public void testFindAllIdsForRepository()
	{
		Mockito.doReturn(newArrayList(createEntity("3"), createEntity("1"), createEntity("2"))).when(abstractRepository)
				.findAll(Matchers.any(Query.class));
		Iterable<Entity> result = abstractRepository.findAll(newArrayList("1", "2", "3", "1", "2"));
		assertEquals(newArrayList(createEntity("1"), createEntity("2"), createEntity("3"), createEntity("1"),
				createEntity("2")), newArrayList(result));

	}

	private Entity createEntity(String id)
	{
		Entity entity = new DefaultEntity(entityMetaData, null);
		entity.set("id", id);
		return entity;
	}
}
