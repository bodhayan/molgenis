package org.molgenis.data.validation;

import static java.util.Objects.requireNonNull;

/**
 * Thrown when updating data that references unexisting data.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class EntityReferenceUnknownConstraintViolationException extends DataIntegrityViolationException
{
	private static final String ERROR_CODE = "V01";

	private final String entityTypeId;
	private final String attributeName;
	private final String valueAsString;

	public EntityReferenceUnknownConstraintViolationException(String entityTypeId, String attributeName,
			String valueAsString, Throwable cause)
	{
		super(ERROR_CODE, cause);
		this.entityTypeId = requireNonNull(entityTypeId);
		this.attributeName = requireNonNull(attributeName);
		this.valueAsString = requireNonNull(valueAsString);
	}

	@Override
	public String getMessage()
	{
		return String.format("type:%s attribute:%s value: %s", entityTypeId, attributeName, valueAsString);
	}

	@Override
	protected Object[] getLocalizedMessageArguments()
	{
		return new Object[] { valueAsString, attributeName, entityTypeId };
	}
}