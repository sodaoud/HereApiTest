package io.sodaoud.heretest.app;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.sodaoud.heretest.app.view.RouteView;


@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    HereTestApplication mockApplication;

    @Mock
    RouteView view;


    @Test
    public void readStringFromContext_LocalizedString() {

    }
}