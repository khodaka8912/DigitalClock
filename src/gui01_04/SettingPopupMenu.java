package gui01_04;

import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class SettingPopupMenu extends PopupMenu {

	private SettingsListener listener;

	private final List<Integer> fontSizes = Settings.getFontSizeList();
	private final List<String> fonts = Settings.getFontList();
	private final Map<String, Color> colorMap = Settings.getColorMap();

	private SelectableMenu fontMenu;
	private SelectableMenu fontSizeMenu;
	private CheckboxMenuItem boldMenu;
	private CheckboxMenuItem itaricMenu;
	private SelectableMenu fontColorMenu;
	private SelectableMenu bgColorMenu;
	private MenuItem exitMenu;

	public SettingPopupMenu() {
		fontMenu = new SelectableMenu(Consts.Strings.FONT, fonts);
		fontSizeMenu = new SelectableMenu(Consts.Strings.FONT_SIZE, fontSizes);
		fontColorMenu = new SelectableMenu(Consts.Strings.FONT_COLOR, colorMap.keySet());
		bgColorMenu = new SelectableMenu(Consts.Strings.BG_COLOR, colorMap.keySet());
		boldMenu = new CheckboxMenuItem(Consts.Strings.BOLD);
		boldMenu.addItemListener(settingItemListener);
		itaricMenu = new CheckboxMenuItem(Consts.Strings.ITALIC);
		itaricMenu.addItemListener(settingItemListener);
		exitMenu = new MenuItem(Consts.Strings.EXIT);
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
		add(exitMenu);
	}

	public void setSettingsListener(SettingsListener listener) {
		this.listener = listener;
	}

	private void settingsChanged() {
		String fontName = fontMenu.getSelectedLabel();
		int fontSize = Integer.parseInt(fontSizeMenu.getSelectedLabel());
		Color fontColor = colorMap.get(fontColorMenu.getSelectedLabel());
		Color bgColor = colorMap.get(bgColorMenu.getSelectedLabel());
		boolean bold = boldMenu.getState();
		boolean italic = itaricMenu.getState();
		listener.onSettingsChanged(new Settings(fontName, bold, italic, fontSize, fontColor, bgColor));
	}

	public void setSelection(Settings current) {
		fontMenu.select(current.fontName);
		fontSizeMenu.select(Integer.toString(current.fontSize));
		fontColorMenu.select(Settings.getColorName(current.fontColor));
		bgColorMenu.select(Settings.getColorName(current.bgColor));
		boldMenu.setState(current.bold);
		itaricMenu.setState(current.italic);
	}

	private ItemListener settingItemListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			settingsChanged();
		}
	};

	private class SelectableMenu extends Menu implements ItemListener {

		private String selected;

		SelectableMenu(String label, Collection<?> subLabels) {
			super(label);
			subLabels.stream().forEach(e -> {
				CheckboxMenuItem item = new CheckboxMenuItem(e.toString());
				item.addItemListener(this);
				add(item);
			});
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			select((String) e.getItem());
			settingsChanged();
		}

		public void select(String label) {
			this.selected = label;
			IntStream.range(0, getItemCount()).forEach(i -> {
				CheckboxMenuItem item = (CheckboxMenuItem) getItem(i);
				item.setState(item.getLabel().equals(label));
			});
		}

		public String getSelectedLabel() {
			return selected;
		}
	}
}
