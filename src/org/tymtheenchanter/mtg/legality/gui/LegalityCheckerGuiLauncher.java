package org.tymtheenchanter.mtg.legality.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author tim
 */
public class LegalityCheckerGuiLauncher
{
	public static void main(String[] args)
	{
		new LegalityCheckerGuiLauncher();
	}

	public LegalityCheckerGuiLauncher()
	{
		createGui();
	}

	private void createGui()
	{
		Display display = new Display();

		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));

		LegalityCheckerGuiControl legalityCheckerGui = new LegalityCheckerGuiControl(shell);
		legalityCheckerGui.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		shell.pack();
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
		display.dispose();
	}
}
