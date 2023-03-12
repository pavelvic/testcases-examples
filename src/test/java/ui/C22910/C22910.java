package ui.C22910;


@SuiteName("1_SMOKE ТЕСТИРОВАНИЕ")
@TestName("09_Прейскуранты")
public class C22910 extends BaseUITest{

	private C22910_TestHelper helper;

	@BeforeMethod
	@Step("Предусловия:")
	public void setUpC22910() {
		helper = new C22910_TestHelper(app);
	}

	@DisplayedName("ADE")
	@Description(
		"<b>Precondition:</b>\n\n" +
		"<i>В системе есть прейскурант в статусе \"Черновик\" </i>\n\n" +
		"<b>Steps:</b>\n\n" +
		"01 - <i>Войти в систему\nВход выполнен</i>\n\n" +
		"02 - <i>Перейти на левой панели в раздел \"Прейскуранты\" -> ADE\nОткрыта вкладка с прейскурантом ADE</i>\n\n" +
		"03 - <i>В выпадающем списке прейскурантов выбрать прейскурант из предусловия (в статусе \"Черновик\")\nПрейскурант выбран</i>\n\n" +
		"04 - <i>Нажать на кнопку \"Ввести в действие\"\nОткрыто окно \"Ввод в действие\" с:\n- неактивным полем \"Наименование прейскуранта\", в котором отображется \"ADE\"\n- неактивным полем \"Дата начала\", в котором отображается сегодняшняя дата\n- активным поле \"Комментарий\"\n- кнопками \"Отмена\" и \"ОК\"\n![](index.php?/attachments/get/10670)</i>\n\n" +
		"05 - <i>Ввести значение в поле \"Комментарий\"\nЗначение введено</i>\n\n" +
		"06 - <i>Нажать на кнопку \"Отмена\"\nОкно закрыто</i>\n\n" +
		"07 - <i>Нажать на кнопку \"Ввести в действие\"\nОткрыто окно \"Ввода в действие\", поле \"Комментарий\" пустое</i>\n\n" +
		"08 - <i>Ввести значение в поле \"Комментарий\"\nЗначение введено</i>\n\n" +
		"09 - <i>Нажать на кнопку \"ОК\"\nПоявляется лоадер и далее сообщение \"Прейскурант введен в действие успешно\"\n![](index.php?/attachments/get/10672)\n\n</i>\n\n" +
		"10 - <i>Обновить страницу\nВведенный в действие прейскурант отображается в списке с зеленым кружком и проставлена сегодняшняя дата ввода в действие</i>\n\n" +
		"11 - <i>Перейти в БД [PriceLists].[dbo].[PriceListVersion]\nДля введенного в действие прейскуранта из предусловия:\n- статус изменен на \"0\"\n- комментарий отображается в поле [UsersComment]\n- в поле [ActivatonDate] отображается сегодняшняя дата и время ввода в действие прейскуранта\n- в поле [StartDate]отображается сегодняшняя дата и время ввода в действие прейскуранта</i>")
	@Test(description="22910: Ввод в действие прейскуранта ADE")
	@TmsLink("22910")
	public void C22910Test(){
		step_01();
		step_02();
		step_03();
		step_04();
		step_05();
		step_06();
		step_07();
		step_08();
		step_09();
		step_10();
		step_11();
	}

	@Step("Шаг 1:")
	private void step_01(){
		helper.doLoginMat();
	}

	@Step("Шаг 2:")
	private void step_02(){
		helper.navigateToПрейскуранты();
		helper.selectTab();
		helper.precondition();
	}

	@Step("Шаг 3:")
	private void step_03(){
		helper.selectDraft();
	}

	@Step("Шаг 4:")
	private void step_04(){
		helper.assertThatEnactmentWindowIsExist();
	}

	@Step("Шаг 5:")
	private void step_05(){
		helper.assertThatCommentCanChanges();
	}

	@Step("Шаг 6:")
	private void step_06(){
		helper.assertThatCancelButtonWorks();
	}

	@Step("Шаг 7:")
	private void step_07(){
		helper.assertThatCommentIsEmptyAfterReopen();
	}

	@Step("Шаг 8:")
	private void step_08(){
		helper.assertThatCommentCanChanges();
	}

	@Step("Шаг 9:")
	private void step_09(){
		helper.assertThatPriceCanEnactment();
	}

	@Step("Шаг 10:")
	private void step_10(){
		helper.assertThatEnactmentIsSuccess();
	}

	@Step("Шаг 11:")
	private void step_11(){
		helper.assertThatDbIsChanged();
	}
}
