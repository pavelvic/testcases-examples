package ui.C22911;

public class C22911_TestHelper extends BaseUIHelper {
	@Instance
	private PriceListHelper helper;
	private final PriceListsPage.ADE_MatTab tab = app.pages.priceListsPage.ADE_MatTab;

	public C22911_TestHelper(AppManager app) {
		super(app);
	}

	public void selectTab() {
		tab.select();
		Until.assertTimeout(5000)
				.assertThat(tab, isChecked());
	}

	public void precondition() {
		if(!helper.hasPriceOfType(ADE, DRAFT)) {
			helper.createAde();
			Until.assertTimeout(10000)
					.assertThat(app.notificationsMat.Успешно_MatPopup, isExist());
			app.notificationsMat.Успешно_MatPopup.close();
		}
		helper.assertThatHasPrice(ADE, DRAFT);
	}

	public void doubleTab() {
		app.windowsHelper.openNewWindow();
		app.pages.priceListsPage.navigateTo();
		tab.select();
		assertThat(tab, isChecked());
	}

	public void assertThatDraftIsPresent() {
		tab.versionSelect.selectDraft();
		Until.assertTimeout(5000)
				.assertThat("Выбран черновик прайс-листа",
						tab.versionSelect.getSelectedItem(), isDraft());
	}

	public void assertThatEnactmentIsSucceed() {
		tab.ВВЕСТИВДЕЙСТВИЕ_MatButton.click();
		Until.assertTimeout(5000)
				.assertThat(app.windows.ВВЕСТИВДЕЙСТВИЕ_MatModalWindow, isExist());
		app.windows.ВВЕСТИВДЕЙСТВИЕ_MatModalWindow.okButton.click();
		Until.assertTimeout(5000)
				.assertThat("Имеется сообщение об успешном вводе в действие прейскуранта",
						app.notificationsMat.Успешно_MatPopup, isExist());
		assertThat(app.notificationsMat.Успешно_MatPopup, text("Прейскурант введен в действие успешно!"));
		app.notificationsMat.Успешно_MatPopup.close();
	}

	public void changeTab() {
		app.windowsHelper.switchToDefaultWindow();
		assertThat("Открыта первая вкладка",
				app.windowsHelper.isDefaultWindow(), Is.is(true));
	}

	public void assertThatErrorMessageIsPresent() {
		tab.ВВЕСТИВДЕЙСТВИЕ_MatButton.click();
		Until.assertTimeout(5000)
				.assertThat(app.windows.ВВЕСТИВДЕЙСТВИЕ_MatModalWindow, isExist());
		app.windows.ВВЕСТИВДЕЙСТВИЕ_MatModalWindow.okButton.click();
		Until.assertTimeout(5000)
				.assertThat("Имеется сообщение ошибке ввода в действе с сообщением ",
						app.notificationsMat.Ошибка_MatModal,text("Возможно, кто-то уже ввел в действие этот прейскурант"));
		app.notificationsMat.Успешно_MatPopup.close();
	}
}
