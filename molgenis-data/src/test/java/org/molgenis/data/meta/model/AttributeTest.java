package org.molgenis.data.meta.model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.molgenis.data.meta.AttributeType.BOOL;
import static org.molgenis.data.meta.AttributeType.STRING;
import static org.molgenis.data.meta.AttributeType.XREF;
import static org.molgenis.data.meta.model.AttributeMetadata.IS_AGGREGATABLE;
import static org.molgenis.data.meta.model.AttributeMetadata.IS_AUTO;
import static org.molgenis.data.meta.model.AttributeMetadata.IS_ID_ATTRIBUTE;
import static org.molgenis.data.meta.model.AttributeMetadata.IS_LABEL_ATTRIBUTE;
import static org.molgenis.data.meta.model.AttributeMetadata.IS_NULLABLE;
import static org.molgenis.data.meta.model.AttributeMetadata.IS_READ_ONLY;
import static org.molgenis.data.meta.model.AttributeMetadata.IS_UNIQUE;
import static org.molgenis.data.meta.model.AttributeMetadata.IS_VISIBLE;
import static org.molgenis.data.meta.model.AttributeMetadata.PARENT;
import static org.molgenis.data.meta.model.AttributeMetadata.TYPE;
import static org.molgenis.data.meta.model.AttributeMetadata.getIdAttributeValidationExpression;
import static org.molgenis.data.meta.model.EntityType.AttributeCopyMode.SHALLOW_COPY_ATTRS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AttributeTest {
  private Attribute attribute;

  @BeforeMethod
  public void setUpBeforeMethod() {
    EntityType entityType = mock(EntityType.class);
    Attribute typeAttr = when(mock(Attribute.class).getDataType()).thenReturn(STRING).getMock();
    Attribute isNullableAttr = when(mock(Attribute.class).getDataType()).thenReturn(BOOL).getMock();
    Attribute isAutoAttr = when(mock(Attribute.class).getDataType()).thenReturn(BOOL).getMock();
    Attribute isVisibleAttr = when(mock(Attribute.class).getDataType()).thenReturn(BOOL).getMock();
    Attribute isAggregatableAttr =
        when(mock(Attribute.class).getDataType()).thenReturn(BOOL).getMock();
    Attribute isReadOnlyAttr = when(mock(Attribute.class).getDataType()).thenReturn(BOOL).getMock();
    Attribute isUniqueAttr = when(mock(Attribute.class).getDataType()).thenReturn(BOOL).getMock();
    Attribute isIdAttr = when(mock(Attribute.class).getDataType()).thenReturn(BOOL).getMock();
    Attribute parentAttr = when(mock(Attribute.class).getDataType()).thenReturn(XREF).getMock();
    Attribute isLabelAttribute =
        when(mock(Attribute.class).getDataType()).thenReturn(BOOL).getMock();
    when(entityType.getAttribute(TYPE)).thenReturn(typeAttr);
    when(entityType.getAttribute(IS_NULLABLE)).thenReturn(isNullableAttr);
    when(entityType.getAttribute(IS_AUTO)).thenReturn(isAutoAttr);
    when(entityType.getAttribute(IS_VISIBLE)).thenReturn(isVisibleAttr);
    when(entityType.getAttribute(IS_AGGREGATABLE)).thenReturn(isAggregatableAttr);
    when(entityType.getAttribute(IS_READ_ONLY)).thenReturn(isReadOnlyAttr);
    when(entityType.getAttribute(IS_UNIQUE)).thenReturn(isUniqueAttr);
    when(entityType.getAttribute(IS_ID_ATTRIBUTE)).thenReturn(isIdAttr);
    when(entityType.getAttribute(PARENT)).thenReturn(parentAttr);
    when(entityType.getAttribute(IS_LABEL_ATTRIBUTE)).thenReturn(isLabelAttribute);

    attribute = new Attribute(entityType);
  }

  @Test
  public void setParentNullToAttribute() {
    Attribute parentAttr = mock(Attribute.class);
    attribute.setParent(parentAttr);
    verify(parentAttr).addChild(attribute);
    verifyNoMoreInteractions(parentAttr);
  }

  @Test
  public void setParentNullToNull() {
    attribute.setParent(null);
    assertNull(attribute.getParent());
  }

  @Test
  public void setParentAttributeToNull() {
    Attribute parentAttr = mock(Attribute.class);
    attribute.setParent(parentAttr);
    verify(parentAttr).addChild(attribute);

    attribute.setParent(null);
    assertNull(attribute.getParent());
    verify(parentAttr).removeChild(attribute);
    verifyNoMoreInteractions(parentAttr);
  }

  @Test
  public void setParentAttributeToAttribute() {
    Attribute currentParentAttr = mock(Attribute.class);
    attribute.setParent(currentParentAttr);
    verify(currentParentAttr).addChild(attribute);

    Attribute parentAttr = mock(Attribute.class);
    attribute.setParent(parentAttr);
    assertEquals(attribute.getParent(), parentAttr);

    verify(currentParentAttr).removeChild(attribute);
    verifyNoMoreInteractions(currentParentAttr);
    verify(parentAttr).addChild(attribute);
    verifyNoMoreInteractions(parentAttr);
  }

  @Test
  public void setIdAttributeTrue() {
    attribute.setIdAttribute(true);
    assertTrue(attribute.isReadOnly());
    assertTrue(attribute.isUnique());
    assertFalse(attribute.isNillable());
  }

  @Test
  public void setIdAttributeFalse() {
    attribute.setIdAttribute(false);
    assertFalse(attribute.isReadOnly());
    assertFalse(attribute.isUnique());
    assertTrue(attribute.isNillable());
  }

  @Test
  public void testIdValidationExpression() {
    String expression = getIdAttributeValidationExpression();
    assertEquals(
        expression,
        "$('isIdAttribute').eq(false).or($('isIdAttribute').isNull()).or($('isIdAttribute').eq(true)"
            + ".and("
            + "$('type').eq('email')"
            + ".or($('type').eq('hyperlink'))"
            + ".or($('type').eq('int'))"
            + ".or($('type').eq('long'))"
            + ".or($('type').eq('string'))"
            + ".or($('type').isNull())"
            + ")"
            + ".and($('isNullable').eq(false))).value()");
  }

  // Regression test for https://github.com/molgenis/molgenis/issues/6566
  @Test
  public void testNewInstance() {
    AttributeFactory attributeFactory = mock(AttributeFactory.class);
    when(attributeFactory.create()).thenReturn(mock(Attribute.class));

    Attribute attribute = mock(Attribute.class);
    when(attribute.isVisible()).thenReturn(true);
    when(attribute.getNullableExpression()).thenReturn("nullableExpression");
    when(attribute.getValidationExpression()).thenReturn("expression");
    Attribute attributeCopy =
        Attribute.newInstance(attribute, SHALLOW_COPY_ATTRS, attributeFactory);
    verify(attributeCopy).setVisible(true);
    verify(attributeCopy).setNullableExpression("nullableExpression");
    verify(attributeCopy).setValidationExpression("expression");
  }
}
