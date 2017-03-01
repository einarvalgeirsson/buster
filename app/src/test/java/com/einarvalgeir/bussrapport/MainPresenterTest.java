package com.einarvalgeir.bussrapport;

import com.einarvalgeir.bussrapport.model.Report;
import com.einarvalgeir.bussrapport.presenter.MainPresenter;
import com.einarvalgeir.bussrapport.util.DateUtil;
import com.einarvalgeir.bussrapport.util.SharedPrefsUtil;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MainPresenterTest {

    MainPresenter presenter;
    Report report;
    SharedPrefsUtil mockSharedPrefs;


    @Before public void before_test() {
        mockSharedPrefs = mock(SharedPrefsUtil.class);
        report = new Report();
    }

    @After public void after_test() {
        report = null;
    }

    @Test
    public void assignee_email_address_is_added_to_report() {
        String mockEmail = "mock@email.com";
        when(mockSharedPrefs.getAssigneeEmail()).thenReturn(mockEmail);
        Report report = new Report();

        presenter = new MainPresenter(report, mockSharedPrefs);

        assertThat(presenter.getEmail().getTo()).isEqualTo(mockEmail);
    }

    @Test
    public void reporter_name_is_added_to_report() {
        String mockReporterName = "Einar Valgeirsson";
        when(mockSharedPrefs.getReporterName()).thenReturn(mockReporterName);
        Report report = new Report();

        presenter = new MainPresenter(report, mockSharedPrefs);

        assertThat(presenter.getReport().getReporterName()).isEqualTo(mockReporterName);
    }

    @Test
    public void setting_bus_nbr_adds_it_to_report() {
        int busNumber = 1234;
        presenter = new MainPresenter(report, mockSharedPrefs);

        presenter.addBusNumber(busNumber);

        assertThat(presenter.getReport().getBusNumber()).isEqualTo(busNumber);
    }

    @Test
    public void setting_service_nbr_adds_it_to_report() {
        int serviceNbr = 4321;
        presenter = new MainPresenter(report, mockSharedPrefs);

        presenter.addServiceNumber(serviceNbr);

        assertThat(presenter.getReport().getServiceNumber()).isEqualTo(serviceNbr);
    }

    @Test
    public void setting_date_adds_it_to_report() {
        int year = 2017;
        int month = 5;
        int day = 9;

        DateTime date = DateUtil.createFormattedDate(year, month, day);
        presenter = new MainPresenter(report, mockSharedPrefs);

        presenter.setDate(date);

        assertThat(presenter.getReport().getTimeOfReporting())
                .isEqualTo(year + "-0" + month + "-0" + day);
    }

    @Test
    public void setting_image_path_adds_it_to_report() {
        String pathToImage = "/somepath/img.png";
        presenter = new MainPresenter(report, mockSharedPrefs);

        presenter.setImage(pathToImage);

        assertThat(presenter.getReport().getImage()).isEqualTo(pathToImage);
    }

    









}
