//package com.nhnacademy.book.config;
//
//import com.nhnacademy.book.skm.properties.SKMProperties;
//import com.nhnacademy.book.skm.service.SecureKeyManagerService;
//import org.apache.commons.dbcp2.BasicDataSource;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import javax.sql.DataSource;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class DataSourceConfigTest {
//
//
//    @Mock
//    private SKMProperties skmProperties;
//
//    @Mock
//    private SecureKeyManagerService secureKeyManagerService;
//
//    @InjectMocks
//    private DataSourceConfig dataSourceConfig;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        SKMProperties.Database database = mock(SKMProperties.Database.class);
//        when(database.getUrl()).thenReturn("encryptedUrlKey");
//        when(database.getUsername()).thenReturn("encryptedUsernameKey");
//        when(database.getPassword()).thenReturn("encryptedPasswordKey");
//
//        when(skmProperties.getDatabase()).thenReturn(database);
//        when(secureKeyManagerService.fetchSecret("encryptedUrlKey")).thenReturn("jdbc:mysql://localhost:3306/testdb");
//        when(secureKeyManagerService.fetchSecret("encryptedUsernameKey")).thenReturn("testuser");
//        when(secureKeyManagerService.fetchSecret("encryptedPasswordKey")).thenReturn("testpassword");
//    }
//
//    @Test
//    @DisplayName("DataSource 빈 생성 테스트")
//    void dataSourceBeanCreationTest() {
//        DataSource dataSource = dataSourceConfig.dataSource();
//
//        assertNotNull(dataSource, "DataSource는 null이 아니어야 합니다.");
//
//        BasicDataSource basicDataSource = (BasicDataSource) dataSource;
//        assertEquals("jdbc:mysql://localhost:3306/testdb", basicDataSource.getUrl(), "URL이 예상값과 일치해야 합니다.");
//        assertEquals("testuser", basicDataSource.getUsername(), "Username이 예상값과 일치해야 합니다.");
//        assertEquals("testpassword", basicDataSource.getPassword(), "Password가 예상값과 일치해야 합니다.");
//    }
//
//}