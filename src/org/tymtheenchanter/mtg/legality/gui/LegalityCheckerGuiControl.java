package org.tymtheenchanter.mtg.legality.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.randomcoding.mtg.tools.enumerations.MagicDeckFormat;
import org.randomcoding.mtg.tools.enumerations.MagicLegalityRestriction;
import org.randomcoding.mtg.tools.legalitychecker.scraper.GathererDataScraper;

/**
 * Composite based control that creates the simple gui for the single card legality checker
 * 
 * @author tym
 */
public class LegalityCheckerGuiControl extends Composite
{
	private Text cardNameText;
	private Table legalityTable;
	private GathererDataScraper legalityScraper;
	private static final Logger log = Logger.getLogger(LegalityCheckerGuiLauncher.class.getCanonicalName());

	public LegalityCheckerGuiControl(Composite parent)
	{
		super(parent, SWT.NONE);
		legalityScraper = new GathererDataScraper();
		createGui();
	}

	private void createGui()
	{
		Display display = new Display();

		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));

		addCardNameBox(shell);
		addLegalityWindow(shell);

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

	private void addCardNameBox(Composite parent)
	{
		cardNameText = new Text(parent, SWT.BORDER);
		cardNameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		final Button checkButton = new Button(parent, SWT.PUSH);
		checkButton.setText("Check Legality");
		cardNameText.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{
				checkButton.setEnabled(cardNameText.getText() != null && !cardNameText.getText().trim().isEmpty());
			}
		});

		checkButton.addSelectionListener(new SelectionAdapter()
		{
			/**
			 * {@inheritDoc}
			 */
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				checkLegality(getCardName());
			}
		});
	}

	protected void checkLegality(String cardName)
	{
		try
		{
			showLegality(legalityScraper.getLegality(cardName));
		}
		catch (IOException e)
		{
			log.warning(e.getMessage());
		}
	}

	private void showLegality(Map<MagicDeckFormat, MagicLegalityRestriction> legality)
	{
		legalityTable.removeAll();
		for (Map.Entry<MagicDeckFormat, MagicLegalityRestriction> restriction : legality.entrySet())
		{
			TableItem item = new TableItem(legalityTable, SWT.TOP);
			item.setText(0, restriction.getKey().name());
			item.setText(1, restriction.getValue().name());
		}

		for (int i = 0; i < 2; i++)
		{
			legalityTable.getColumn(i).pack();
		}
	}

	protected String getCardName()
	{
		return cardNameText.getText();
	}

	private void addLegalityWindow(Composite parent)
	{
		legalityTable = new Table(parent, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		legalityTable.setLinesVisible(true);
		legalityTable.setHeaderVisible(true);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		gridData.widthHint = 300;
		gridData.heightHint = 200;
		legalityTable.setLayoutData(gridData);

		TableColumn legalityColumn = new TableColumn(legalityTable, SWT.TOP);
		legalityColumn.setText("Legality");

		TableColumn deckColumn = new TableColumn(legalityTable, SWT.NONE);
		deckColumn.setText("Deck Format");
		showLegality(new HashMap<MagicDeckFormat, MagicLegalityRestriction>());
		legalityTable.pack();
	}
}
