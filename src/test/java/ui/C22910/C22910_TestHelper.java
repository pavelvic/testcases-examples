package ui.C22910;

public class C22910_TestHelper extends BaseUIHelper {

	@Instance
	private PriceListHelper helper;
	private final PriceListsPage.ADE_MatTab tab = app.pages.priceListsPage.ADE_MatTab;
	private final WindowsManager.ActivateMatModalWindow window = app.windows.ВВЕСТИВДЕЙСТВИЕ_MatModalWindow;
	private String nameOfDraft;
	private int draftId;
	private String comment;
	private Date startDate;

	public C22910_TestHelper(AppManager app) {
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
		nameOfDraft = tab.versionSelect.getSelectedItem().getText();
		draftId = helper.getPriceIDFromDb(ADE,DRAFT);
	}

	public void selectDraft() {
		tab.versionSelect.selectDraft();
		Until.assertTimeout(5000)
				.assertThat("Выбран черновик прайс-листа",
						tab.versionSelect.getSelectedItem(), isDraft());
	}

	public void assertThatEnactmentWindowIsExist() {
		tab.ВВЕСТИВДЕЙСТВИЕ_MatButton.click();

		Until.assertTimeout(5000)
				.assertThat("Открылось окно Ввести в действие",window, isExist());
		assertThat("Имеется поле Наименование прейскуранта",
				window.Наименование_MatInputText, isExist());
		assertThat("Название поля с наименованием прейскуранта = Наименование прейскуранта",
				window.Наименование_MatInputText.getLabel(), Is.is("Наименование прейскуранта"));
		assertThat("Поле Наименование прейскуранта недоступно для редактирования",
				window.Наименование_MatInputText, not(isEnabled()));
		assertThat("Поле наименование прейскуранта содержит название вводимого в действие прейскуранта",
				window.Наименование_MatInputText, text(nameOfDraft));
		assertThat("Имеется поле Дата начала действия",
				window.ДатаНачалаДействия_MatInputDate, isExist());
		assertThat("Название поля с датой начала действия прейскуранта = Дата начала действия",
				window.ДатаНачалаДействия_MatInputDate.getLabel(), Is.is("Дата начала действия"));
		assertThat("Поле Дата начала действия недоступно для редактирования",
				window.ДатаНачалаДействия_MatInputDate, not(isEnabled()));
		startDate = SimpleDateHelper.getCurrentDate().toDate();
		assertThat("Поле Дата начала действия содержит сегодняшнюю дату в формате ДД.ММ.ГГГГ",
				window.ДатаНачалаДействия_MatInputDate, isEqual(startDate));
		assertThat("Имеется поле Комментарий",
				window.Комментарий_MatTextArea, isExist());
		assertThat("Название поля комментарием = Комментарий",
				window.Комментарий_MatTextArea.getLabel(), Is.is("Комментарий"));
		assertThat("Поле Комментарий доступно для редактирования",
				window.Комментарий_MatTextArea, isEnabled());
		assertThat("Имеется кнопка для ввода в действие",
				window.okButton, isExist());
		assertThat("Название кнопки для ввода в действие - ОК",
				window.okButton, text("ОК"));
		assertThat("Имеется кнопка для отмены (закрытие окна)",
				window.cancelButton, isExist());
		assertThat("Название кнопки для отмены (закрытия окна) - ОТМЕНА",
				window.cancelButton, text("ОТМЕНА"));
	}

	public void assertThatCommentCanChanges() {
		comment = LocalDateTime.now(ZoneId.of("Europe/Moscow")).toString();
		window.Комментарий_MatTextArea.setText(comment);
		assertThat("Введён текст " + comment+ " в поле 'Комментарий'",
				window.Комментарий_MatTextArea, text(comment));
	}

	public void assertThatCancelButtonWorks() {
		window.cancelButton.click();
		Until.assertTimeout(5000)
				.assertThat("Окно ввода в действие закрылось",
						window, not(isExist()));
	}

	public void assertThatCommentIsEmptyAfterReopen() {
		tab.ВВЕСТИВДЕЙСТВИЕ_MatButton.click();
		Until.assertTimeout(5000)
				.assertThat("Открылось окно ввода в действие",
						window, isExist());
		assertThat("Поле Комментарий - пустое",
				window.Комментарий_MatTextArea, text(""));
	}

	public void assertThatPriceCanEnactment() {
		window.okButton.click();
		Until.assertTimeout(5000)
				.assertThat("Имеется сообщение об успешном вводе в действие прейскуранта",
						app.notificationsMat.Успешно_MatPopup, isExist());
		assertThat(app.notificationsMat.Успешно_MatPopup, text("Прейскурант введен в действие успешно!"));
		app.notificationsMat.Успешно_MatPopup.close();
	}

	public void assertThatEnactmentIsSuccess() {
		app.getDriver().navigate().refresh();
		tab.versionSelect.selectActive();
		var date = SimpleDateHelper.dateOf(tab.versionSelect.getSelectedItem().getDate()).setFormat("dd.MM.yyyy");
		var refName = nameOfDraft+" от "+date;
		assertThat("Черновик прайслиста "+nameOfDraft+" введён в действие и отображается в списке с датой ввода в действие",
				tab.versionSelect.getSelectedItem(), text(refName));
		assertThat("Введенный в действие прейскурант отображается с зелёным кружком",
				tab.versionSelect.getSelectedItem(), isGreen());
	}

	public void assertThatDbIsChanged() {
		String query = String.format("SELECT plv.PriceListStatusId, plv.UsersComment, plv.ActivationDate, plv.StartDate  \n" +
				"FROM PriceLists.dbo.PriceListVersion plv\n" +
				"WHERE plv.Id = %s",draftId);
		var result = app.db.forQuery(query);
		var statusId = result.getString(1);
		var commentInDb = result.getString(2);
		var activationDateInDb = SimpleDateHelper.dateOf(result.getDate(3)).setFormat("dd.MM.yyyy").toDate();
		var startDateInDb = SimpleDateHelper.dateOf(result.getDate(4)).setFormat("dd.MM.yyyy").toDate();

		assertThat("PriceLists.dbo.PriceListVersion.PriceListStatusId = '0'",
				statusId, Is.is("0"));
		assertThat("PriceLists.dbo.PriceListVersion.UsersComment = '"+comment+"'",
				commentInDb, Is.is(comment));
		assertThat("Дата активации в БД равна расчетной дате начала действия = сегодня",
				activationDateInDb, Is.is(startDate));
		assertThat("Дата начала действия прекйскуранта в БД равна расчётной дате начала действия = сегодня",
				startDateInDb, Is.is(startDate));
	}
}