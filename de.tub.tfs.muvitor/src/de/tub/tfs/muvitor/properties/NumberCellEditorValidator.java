package de.tub.tfs.muvitor.properties;

import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * Validator for Strings being castable to an Integer.
 * 
 * @author Tony Modica
 */
public class NumberCellEditorValidator implements ICellEditorValidator {
	
	private static NumberCellEditorValidator instance;
	
	private static NumberCellEditorValidator instancePositive;
	
	public static NumberCellEditorValidator instance(final boolean positive) {
		if (positive) {
			if (NumberCellEditorValidator.instancePositive == null) {
				NumberCellEditorValidator.instancePositive = new NumberCellEditorValidator(positive);
			}
			return NumberCellEditorValidator.instancePositive;
		}
		if (NumberCellEditorValidator.instance == null) {
			NumberCellEditorValidator.instance = new NumberCellEditorValidator(positive);
		}
		return NumberCellEditorValidator.instance;
		
	}
	
	private final boolean positive;
	
	public NumberCellEditorValidator(final boolean positive) {
		this.positive = positive;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	@Override
	public String isValid(final Object value) {
		try {
			final int val = new Integer((String) value).intValue();
			if (val >= 0 || !positive) {
				return null;
			}
			return "the value is not a positive number";
		} catch (final Exception exc) {
			return "the value is not a number";
		}
	}
}
