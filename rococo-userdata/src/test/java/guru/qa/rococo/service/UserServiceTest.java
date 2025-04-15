package guru.qa.rococo.service;

//@ExtendWith(MockitoExtension.class)
class UserServiceTest {
//    private UserService testedObject;
//
//    private final UUID mainTestUserUuid = UUID.randomUUID();
//    private final String mainTestUserName = "dima";
//    private UserEntity mainTestUser;
//
//    private final UUID secondTestUserUuid = UUID.randomUUID();
//    private final String secondTestUserName = "barsik";
//    private UserEntity secondTestUser;
//
//    private final UUID thirdTestUserUuid = UUID.randomUUID();
//    private final String thirdTestUserName = "emma";
//    private UserEntity thirdTestUser;
//
//
//    private final String notExistingUser = "not_existing_user";
//
//    @BeforeEach
//    void init() {
//        mainTestUser = new UserEntity();
//        mainTestUser.setId(mainTestUserUuid);
//        mainTestUser.setUsername(mainTestUserName);
//        mainTestUser.setCurrency(CurrencyValues.RUB);
//
//        secondTestUser = new UserEntity();
//        secondTestUser.setId(secondTestUserUuid);
//        secondTestUser.setUsername(secondTestUserName);
//        secondTestUser.setCurrency(CurrencyValues.RUB);
//
//        thirdTestUser = new UserEntity();
//        thirdTestUser.setId(thirdTestUserUuid);
//        thirdTestUser.setUsername(thirdTestUserName);
//        thirdTestUser.setCurrency(CurrencyValues.RUB);
//    }
//
//    @ValueSource(strings = {
//            "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAACwVBMVEUanW4am2wBY0QYmWsMfFYBZEQAAQBHcEz+" +
//                    "/v4AAAAZm20anW4bnW4IdFAanW4anW4BZUQbnm8bnm8anW4anG4VflganW4anW4Jd1Ibnm8EbEoDaUcbnW4anW4anW4bnm8anW4anW4" +
//                    "anG4BZUQWlGcHKR0anG0anW4FbksanG4anW4FbEobnm4anW4anW4anW4anW4anW4Zm2wanG0ZmmwZm20BY0MZmmwanW4anG4anG0cnm9B" +
//                    "roj0+vj9/v4hoXIAYkMfoHEMfFYLe1QWlGgTjWIQh14XlWgNf1gNf1gAYUIAYkIAYUIAYUIAYUIKeFMAYUIEakgAYUISimAAYUIAYEEAYUE" +
//                    "AYUIAYUIAYUIBCwcanW4MSjQHLB8BBwUJOCcNTzcSbUwVe1YanW4AAwIPWj8Sakvj8+0LQC0ZmmwAY0MAY0MAYkNdupglonVovp9RtJFbuZg1" +
//                    "qX8Zm2yAyK7f8er3+/p9x6yk2MUDaUi038+Fy7IHck6w3c0KeFMGb0wEakllvZ4IdVAGcE0Le1UKeVMbnm8AAAD///8AYUIanW4Zm20amGsCEAs" +
//                    "GJhoMfFYZmmwOVj0EHBQYj2QBBgQSbEwYjGIam2wUd1MYkmYCDQkFHhUOVDsJdVEJNSUZlWgSbU0Zk2cVkmYABANqv6Hs9vKr28pTtpLn9fBeupn+/" +
//                    "v46q4Mdn3D6/PsDaUcPhFwBZEQIc08AYkIYmWsVfliU0bsAAQALQS0ZlWkOUjkYjmMUeFSb1MB5xqoBCQYDFg8KPSsHKx4Zl2oYkGV3xakanG4NUDgcnnC" +
//                    "OzrcIMiMzqH5IsYv1+vmHzLNauJbF5tq84tRUtpPX7eWg1sJDr4gOgVn6/fyCyrArpXlwwqWm2cfa7+goo3fB5Ng9rIUjoXQhoHOe1sLT7ONLso0OgVrt9/P9/v2" +
//                    "+49XI59zW7eUKeVOYug0QAAAAhnRSTlP+/q/+/gX+AP7+/vYU/QO9kAb7sv7+EcH+helU03EZM/0BIQj+/vv3E+2o/rBEFZlayYYr4Aj9RPrrlv7+/v7+VP71Bf7Zs" +
//                    "PXhfOkTkvUd/ob+OP58/uHz2bD+0P7+/v7+/v7P/v7+/v66r5CQ/v7+/v7+uv7+/v7+kv7+Hf44hpL+HYY4/hahmy4AAAMTSURBVEjHY2BHBqbmRsUmagwMbVCkZpJp" +
//                    "ZG6KooQBiW1sZQlXCkdtDJZWxlg1KGlZMDBg08DQZqGlhKGBT5a7DQ/gluVD1SAmDDEMuw0gJCyGrEEumoGQBoZkOYQGHpk2IoAMD0yDID/cMDw2tDHwC0I1SDIQp4FBEq" +
//                    "JBXgirC7oXTpvcgyokJA/SoCjehmkDV+K8XrZ2zri+iYeR7RNXBGrQVcDUMGMSZzsnBO2bw4jQoKAL1KCD4ZiuOdvakUDfTISUDjuDnjoDmg1dCRCzo/aDXAVkzp4PV6Kux2DA" +
//                    "gK5hARtQFdsBUeY2kWkTZ4H0xiO8YcCgj+6gQ71AZ8zaDeXNPNjePmkLQlafwRDdhlSgmVNE4YI9fTE9SAFlyKCJbsNsoAU7kfjTUWQ1GRjQbJicAgz+Hdhjuo2BAT0VMLTNAPo4F" +
//                    "ipivxybBjQwFeii7RDm40drV2NEEoYNiUAbMsDc6mccHA8JOykJqEEVzG3s4OBY4UjQSfMXt7cvXghirXna0bHqHkEnMagC4+EYWOQ5B0dHLkEnMaS1c87bBRZZBtSwhKCT2o6eXNAFYV" +
//                    "3o6OhYSdhJcGS9lINjlQ2GDd04NawEhtJS9FDiYnBHtm/1OQT7/kugi26hu8idwRWhfU25re1yGPdBLUcHh90V9IhzZXBD8IChwnGmxB7MtnkBDCKOCoy05MbgibDu4lqgIzpWXD5y8+zdayD" +
//                    "mpTsYYejJUOeC8HR+GdBUBLpdhJG8XVoYnD2QQqmgFElD1Q3M/ODhzMBez4wUrHnX0yGq7c4XWmNmIOZmYLlU44TiyOOn927dc2LJKazFp1MTqGytXERsYbyoAVwYa0hNaCMKTJDSgBT30rwixN" +
//                    "ggwisNq4EEJLoIa+iSEIBXWaxMc7sJuad7LhMrolJUztJWwW+DijaTMnK1y5pj9oQLtwauq2bZrGgtAQevdetxuKt7/TovB8ymg7eP7wb/4M3oNmwO9t/g6+ONtXHiFxAWuilw4ysWln4oat0YuCk0" +
//                    "LMAPR2sG6JWgkMiI8E44CI+IDAliRVECAE4WhZg/rX3CAAAAAElFTkSuQmCC",
//            ""
//    })
//    @ParameterizedTest
//    void userShouldBeUpdated(String photo, @Mock UserRepository userRepository) {
//        when(userRepository.findByUsername(eq(mainTestUserName)))
//                .thenReturn(Optional.of(mainTestUser));
//
//        when(userRepository.save(any(UserEntity.class)))
//                .thenAnswer(answer -> answer.getArguments()[0]);
//
//        testedObject = new UserService(userRepository);
//
//        final String photoForTest = photo.isEmpty() ? null : photo;
//
//        final UserJson toBeUpdated = new UserJson(
//                null,
//                mainTestUserName,
//                "Test",
//                "TestSurname",
//                "Test TestSurname",
//                CurrencyValues.USD,
//                photoForTest,
//                null,
//                null
//        );
//        final UserJson result = testedObject.update(toBeUpdated);
//        assertEquals(mainTestUserUuid, result.id());
//        assertEquals("Test TestSurname", result.fullname());
//        assertEquals(CurrencyValues.USD, result.currency());
//        assertEquals(photoForTest, result.photo());
//
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//    }
//
//    @Test
//    void getRequiredUserShouldThrowNotFoundExceptionIfUserNotFound(@Mock UserRepository userRepository) {
//        when(userRepository.findByUsername(eq(notExistingUser)))
//                .thenReturn(Optional.empty());
//
//        testedObject = new UserService(userRepository);
//
//        final NotFoundException exception = assertThrows(NotFoundException.class,
//                () -> testedObject.getRequiredUser(notExistingUser));
//        assertEquals(
//                "Can`t find user by username: '" + notExistingUser + "'",
//                exception.getMessage()
//        );
//    }
//
//    @Test
//    void allUsersShouldReturnCorrectUsersList(@Mock UserRepository userRepository) {
//        when(userRepository.findByUsernameNot(eq(mainTestUserName)))
//                .thenReturn(getMockUsersMappingFromDb());
//
//        testedObject = new UserService(userRepository);
//
//        final List<UserJsonBulk> users = testedObject.allUsers(mainTestUserName, null);
//        assertEquals(2, users.size());
//        final UserJsonBulk invitation = users.stream()
//                .filter(u -> u.friendshipStatus() == INVITE_SENT)
//                .findFirst()
//                .orElseThrow(() -> new AssertionError("Friend with state INVITE_SENT not found"));
//
//        final UserJsonBulk friend = users.stream()
//                .filter(u -> u.friendshipStatus() == null)
//                .findFirst()
//                .orElseThrow(() -> new AssertionError("user without status not found"));
//
//
//        assertEquals(secondTestUserName, invitation.username());
//        assertEquals(thirdTestUserName, friend.username());
//    }
//
//    @Test
//    void allUsersShouldReturnCorrectWithIgnoringBigPhotos(@Mock UserRepository userRepository) {
//
//        final List<UserWithStatus> users = List.of(
//                new UserWithStatus(
//                        UUID.randomUUID(),
//                        "firstUsername",
//                        CurrencyValues.USD,
//                        "firstFullName",
//                        "someByte".getBytes(),
//                        FriendshipStatus.ACCEPTED),
//                new UserWithStatus(
//                        UUID.randomUUID(),
//                        "secondUsername",
//                        CurrencyValues.USD,
//                        "secondFullName",
//                        "someByte".getBytes(),
//                        FriendshipStatus.ACCEPTED)
//        );
//
//        Mockito.when(userRepository.findByUsernameNot(eq(mainTestUserName)))
//                .thenReturn(users);
//
//        testedObject = new UserService(userRepository);
//        List<UserJsonBulk> result = testedObject.allUsers(mainTestUserName, null);
//        Mockito.verify(userRepository, Mockito.times(1))
//                .findByUsernameNot(eq(mainTestUserName));
//
//        assertEquals(2, result.size());
//        assertEquals("someByte", result.getFirst().photoSmall());
//    }
//
//    @Test
//    void shouldTransformEntityToUserJsonIfUserNotFoundInDb(@Mock UserRepository userRepository) {
//
//        Mockito.when(userRepository.findByUsername(eq(mainTestUserName)))
//                .thenReturn(Optional.of(mainTestUser));
//
//        testedObject = new UserService(userRepository);
//        UserJson result = testedObject.getCurrentUser(mainTestUserName);
//
//        verify(userRepository, times(1)).findByUsername(eq(mainTestUserName));
//
//        assertEquals(UserJson.fromEntity(mainTestUser), result);
//    }
//
//    @Test
//    void allUsersShouldReturnWhenUseSearchQueryInRequest(@Mock UserRepository userRepository) {
//
//        final String username = "dick";
//        final List<UserWithStatus> users = List.of(
//                new UserWithStatus(
//                        UUID.randomUUID(),
//                        username,
//                        CurrencyValues.USD,
//                        "firstFullName",
//                        "someByte".getBytes(),
//                        FriendshipStatus.ACCEPTED)
//        );
//
//        when(userRepository.findByUsernameNot(eq("name"), eq(username)))
//                .thenReturn(users);
//        testedObject = new UserService(userRepository);
//        List<UserJsonBulk> result = testedObject.allUsers("name", username);
//
//        assertEquals(1, result.size());
//        assertEquals(username, result.getFirst().username());
//    }
//
//    @Test
//    void pageWithAllUsersShouldReturnWhenUseSearchQueryInRequest(@Mock UserRepository userRepository,
//                                                                 @Mock Pageable pageable) {
//
//        final String username = "dick";
//        final String searchQuery = "search";
//        UserWithStatus userWithStatus = new UserWithStatus(
//                UUID.randomUUID(),
//                username,
//                CurrencyValues.USD,
//                "firstFullName",
//                "someByte".getBytes(),
//                FriendshipStatus.ACCEPTED
//        );
//        Page<UserWithStatus> userPage = new PageImpl<>(Collections.singletonList(userWithStatus), pageable, 1);
//
//        when(userRepository.findByUsernameNot(
//                        eq(username),
//                        eq(searchQuery),
//                        eq(pageable)
//                )
//        ).thenReturn(userPage);
//
//        testedObject = new UserService(userRepository);
//        Page<UserJsonBulk> result = testedObject.allUsers(username, pageable, searchQuery);
//        verify(userRepository, times(1))
//                .findByUsernameNot(eq(username), eq(searchQuery), eq(pageable));
//
//        assertFalse(result.isEmpty());
//        assertEquals(1, result.getTotalElements());
//        assertEquals(UserJsonBulk.fromUserEntityProjection(userWithStatus), result.getContent().getFirst());
//    }
//
//    @Test
//    void shouldCreateFriendshipRequest(@Mock UserRepository userRepository) {
//
//        when(userRepository.findByUsername(eq(mainTestUserName))).thenReturn(Optional.of(mainTestUser));
//        when(userRepository.findByUsername(eq(secondTestUserName))).thenReturn(Optional.of(secondTestUser));
//
//        testedObject = new UserService(userRepository);
//        UserJson result = testedObject.createFriendshipRequest(mainTestUserName, secondTestUserName);
//
//        verify(userRepository, times(1)).save(mainTestUser);
//        assertEquals(secondTestUserName, result.username());
//        assertEquals(INVITE_SENT, result.friendshipStatus());
//        assertEquals(result.id(), mainTestUser.getFriendshipRequests().getFirst().getAddressee().getId());
//    }
//
//    @Test
//    void shouldAcceptFriendshipRequest(@Mock UserRepository userRepository) {
//
//        FriendshipEntity friendshipEntity = new FriendshipEntity();
//        friendshipEntity.setRequester(mainTestUser);
//        friendshipEntity.setAddressee(secondTestUser);
//        friendshipEntity.setStatus(FriendshipStatus.PENDING);
//        secondTestUser.getFriendshipAddressees().add(friendshipEntity);
//
//        when(userRepository.findByUsername(eq(mainTestUserName))).thenReturn(Optional.of(mainTestUser));
//        when(userRepository.findByUsername(eq(secondTestUserName))).thenReturn(Optional.of(secondTestUser));
//
//        testedObject = new UserService(userRepository);
//        final UserJson result = testedObject.acceptFriendshipRequest(secondTestUserName, mainTestUserName);
//
//        verify(userRepository, times(1)).save(secondTestUser);
//        assertNotNull(result);
//        assertEquals(mainTestUser.getId(), result.id());
//        assertEquals(FriendshipStatus.ACCEPTED, secondTestUser.getFriendshipAddressees().getFirst().getStatus());
//    }
//
//    @Test
//    void shouldDeclineFriendshipRequest(@Mock UserRepository userRepository) {
//
//        mainTestUser.addInvitations(secondTestUser);
//        secondTestUser.addFriends(FriendshipStatus.PENDING, mainTestUser);
//
//        when(userRepository.findByUsername(mainTestUserName)).thenReturn(Optional.of(mainTestUser));
//        when(userRepository.findByUsername(secondTestUserName)).thenReturn(Optional.of(secondTestUser));
//
//        testedObject = new UserService(userRepository);
//        UserJson result = testedObject.declineFriendshipRequest(mainTestUserName, secondTestUserName);
//
//        assertNotNull(result);
//        assertEquals(secondTestUser.getUsername(), result.username());
//        assertTrue(mainTestUser.getFriendshipAddressees().isEmpty());
//        assertTrue(secondTestUser.getFriendshipRequests().isEmpty());
//        assertTrue(secondTestUser.getFriendshipAddressees().isEmpty());
//        assertTrue(mainTestUser.getFriendshipRequests().isEmpty());
//    }
//
//    @Test
//    void shouldRemoveFriend(@Mock UserRepository userRepository) {
//
//        mainTestUser.addFriends(FriendshipStatus.ACCEPTED, secondTestUser);
//        secondTestUser.addFriends(FriendshipStatus.ACCEPTED, mainTestUser);
//
//        when(userRepository.findByUsername(mainTestUserName)).thenReturn(Optional.of(mainTestUser));
//        when(userRepository.findByUsername(secondTestUserName)).thenReturn(Optional.of(secondTestUser));
//
//        new UserService(userRepository).removeFriend(mainTestUserName, secondTestUserName);
//
//        assertTrue(mainTestUser.getFriendshipAddressees().isEmpty());
//        assertTrue(secondTestUser.getFriendshipRequests().isEmpty());
//        assertTrue(secondTestUser.getFriendshipAddressees().isEmpty());
//        assertTrue(mainTestUser.getFriendshipRequests().isEmpty());
//    }
//
//    private List<UserWithStatus> getMockUsersMappingFromDb() {
//        return List.of(
//                new UserWithStatus(
//                        secondTestUser.getId(),
//                        secondTestUser.getUsername(),
//                        secondTestUser.getCurrency(),
//                        secondTestUser.getFullname(),
//                        secondTestUser.getPhotoSmall(),
//                        FriendshipStatus.PENDING
//                ),
//                new UserWithStatus(
//                        thirdTestUser.getId(),
//                        thirdTestUser.getUsername(),
//                        thirdTestUser.getCurrency(),
//                        thirdTestUser.getFullname(),
//                        thirdTestUser.getPhotoSmall(),
//                        FriendshipStatus.ACCEPTED
//                )
//        );
//    }
}