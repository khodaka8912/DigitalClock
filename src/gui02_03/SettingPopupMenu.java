package gui02_03;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class SettingPopupMenu extends JPopupMenu {

	private SettingsListener listener;

	private final List<Integer> fontSizes = Settings.getFontSizeList();
	private final List<String> fonts = Settings.getFontList();
	private final Map<String, Color> colorMap = Settings.getColorMap();

	private SelectableMenu fontMenu;
	private SelectableMenu fontSizeMenu;
	private JCheckBoxMenuItem boldMenu;
	private JCheckBoxMenuItem itaricMenu;
	private SelectableMenu fontColorMenu;
	private SelectableMenu bgColorMenu;
	private JMenuItem dialogMenu;
	private JMenuItem exitMenu;

	public SettingPopupMenu(SettingDialog settingDialog) {
		fontMenu = new SelectableMenu(Consts.Strings.FONT, fonts);
		fontSizeMenu = new SelectableMenu(Consts.Strings.FONT_SIZE, fontSizes);
		fontColorMenu = new SelectableMenu(Consts.Strings.FONT_COLOR, colorMap.keySet());
		bgColorMenu = new SelectableMenu(Consts.Strings.BG_COLOR, colorMap.keySet());
		boldMenu = new JCheckBoxMenuItem(Consts.Strings.BOLD);
		boldMenu.addItemListener(settingItemListener);
		itaricMenu = new JCheckBoxMenuItem(Consts.Strings.ITALIC);
		itaricMenu.addItemListener(settingItemListener);
		dialogMenu = new JMenuItem(Consts.Strings.SHOW_DIALOG);
		dialogMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settingDialog.setVisible(true);
			}
		});
		exitMenu = new JMenuItem(Consts.Strings.EXIT);
		exitMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		add(fontMenu);
		add(fontSizeMenu);
		add(boldMenu);
		add(itaricMenu);
		add(fontColorMenu);
		add(bgColorMenu);
//		add(dialogMenu);
		add(exitMenu);
	}

	public void setSettingsListener(SettingsListener listener) {
		this.listener = listener;
	}

	private void settingsChanged() {
		try {
			String fontName = fontMenu.getSelectedLabel();
			int fontSize = Integer.parseInt(fontSizeMenu.getSelectedLabel());
			Color fontColor = colorMap.get(fontColorMenu.getSelectedLabel());
			Color bgColor = colorMap.get(bgColorMenu.getSelectedLabel());
			boolean bold = boldMenu.getState();
			boolean italic = itaricMenu.getState();
			listener.onSettingsChanged(new Settings(fontName, bold, italic, fontSize, fontColor, bgColor));
		} catch (Exception e) {
			// Not initialized
		}
	}

	public void setSelection(Settings current) {
		fontMenu.select(current.fontName);
		fontSizeMenu.select(Integer.toString(current.fontSize));
		fontColorMenu.select(Settings.getColorName(current.fontColor));
		bgColorMenu.select(Settings.getColorName(current.bgColor));
		boldMenu.setState(current.bold);
		itaricMenu.setState(current.itaric);
	}

	private ItemListener settingItemListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			settingsChanged();
		}
	};

	private class SelectableMenu extends JMenu implements ItemListener {

		private String selectedText;

		SelectableMenu(String label, Collection<?> subLabels) {
			super(label);
			subLabels.stream().forEach(e -> {
				JCheckBoxMenuItem item = new JCheckBoxMenuItem(e.toString());
				item.addItemListener(this);
				add(item);
			});
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				select(((JMenuItem) e.getItem()).getText());
				settingsChanged();
			}
		}
		
		void select(String label) {
			selectedText = label;
			IntStream.range(0, getItemCount()).forEach(i -> {
				JCheckBoxMenuItem item = (JCheckBoxMenuItem) getItem(i);
				item.setState(item.getText().equals(label));
			});
		}

		String getSelectedLabel() {
			return selectedText;
		}
	}
}
