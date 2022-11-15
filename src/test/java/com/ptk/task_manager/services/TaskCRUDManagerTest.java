package com.ptk.task_manager.services;

import com.ptk.task_manager.entities.Task;
import com.ptk.task_manager.entities.User;
import com.ptk.task_manager.exceptions.TaskNotFoundException;
import com.ptk.task_manager.repositories.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskCRUDManagerTest {
    // simulate repository storage
    private static final List<Task> fakeSimpleStorage = new ArrayList<>();
    private static TaskRepository taskRepository;
    private static TaskCRUDManager taskCrudManager;
    private static final Map<String, User> users = new HashMap<>();

    @BeforeAll
    public static void generalSetUp() {
        taskRepository = mock(TaskRepository.class);
        taskCrudManager = new TaskCRUDManager(taskRepository);

        users.put("dcosta", new User("dcosta", "123"));
        users.put("ppires", new User("ppires", "456"));
    }

    @BeforeEach
    public void setUp() {
        Task task1 = new Task(1, "task 1", "description 1", false);
        Task task2 = new Task(2, "task 2", "", false);
        Task task3 = new Task(3, "task 3", "description 3", false);

        task1.setOwner(users.get("dcosta"));
        task2.setOwner(users.get("dcosta"));
        task3.setOwner(users.get("ppires"));

        fakeSimpleStorage.add(task1);
        fakeSimpleStorage.add(task2);
        fakeSimpleStorage.add(task3);
    }

    @AfterEach
    public void tearDown() {
        fakeSimpleStorage.clear();
    }

    @Test
    void create() {
        // arrange
        User admin = new User("admin", "admin");
        Task task = new Task(1, "task", "description", false);
        task.setOwner(admin);

        // act
        long id = taskCrudManager.create(task);

        // assert
        verify(taskRepository).save(task);
        assertEquals(task.getId(), id);
    }

    @Test
    void list() {
        // arrange
        when(taskRepository.findAllByOwner_username("dcosta"))
                .thenReturn(fakeSimpleStorage.stream()
                        .filter(it -> it.getOwnerUsername()
                                .equals("dcosta"))
                        .collect(Collectors.toList()));

        List<Task> expectedDcostaTasks = Arrays.asList(
                fakeSimpleStorage.get(0),
                fakeSimpleStorage.get(1)
        );

        // act
        List<Task> actualDcostaTasks = new ArrayList<>(taskCrudManager.list("dcosta"));

        // assert
        assertEquals(expectedDcostaTasks, actualDcostaTasks);
    }

    @Test
    void findTask() {
        // arrange
        when(taskRepository.findById(1))
            .thenReturn(Optional.of(
                fakeSimpleStorage.stream()
                    .filter(it -> it.getId() == 1)
                    .collect(Collectors.toList())
                        .get(0)));

        when(taskRepository.findById(4))
            .thenReturn(Optional.empty());

        // scenarios:
        //      1. find non existing id
        //      2. find existing id for diff owner
        //      3. find existing id for correct owner - best scenario

        // scenario 1
        // act and assert
        assertThrows(TaskNotFoundException.class, () ->
                taskCrudManager.findTask(4, "ppires"));

        // scenario 2
        // act and assert
        assertThrows(TaskNotFoundException.class, () ->
                taskCrudManager.findTask(1, "ppires"));

        // scenario 3
        // act
        Task task = taskCrudManager.findTask(1, "dcosta");
        // assert
        assertEquals(fakeSimpleStorage.get(0), task);
    }

    @Test
    void update() {
        // arrange
        when(taskRepository.findById(2))
            .thenReturn(Optional.of(
                fakeSimpleStorage.stream()
                    .filter(it -> it.getId() == 2)
                    .collect(Collectors.toList())
                        .get(0)));

        when(taskRepository.findById(4))
            .thenReturn(Optional.empty());

        Task task = new Task(2, "task 2", "description 2", false);
        Task nonExistingTask = new Task(4, "non existing task", "", false);

        // scenarios:
        //      1. update non existing id
        //      2. update existing id for diff owner
        //      3. update existing id for correct owner - best scenario

        // scenario 1
        // act and assert
        assertThrows(TaskNotFoundException.class, () ->
                taskCrudManager.update(nonExistingTask));

        // scenario 2
        task.setOwner(users.get("ppires"));
        // act and assert
        assertThrows(TaskNotFoundException.class, () ->
                taskCrudManager.update(task));

        // scenario 3
        // act
        task.setOwner(users.get("dcosta"));
        taskCrudManager.update(task);
        // assert
        assertEquals(task, fakeSimpleStorage.get(1));
    }

    @Test
    void delete() {
        // arrange
        when(taskRepository.findById(2))
            .thenReturn(Optional.of(
                fakeSimpleStorage.stream()
                    .filter(it -> it.getId() == 2)
                    .collect(Collectors.toList())
                        .get(0)));

        when(taskRepository.findById(4))
            .thenReturn(Optional.empty());

        doAnswer(invocation -> {
            fakeSimpleStorage.remove(1);
            return null;
        }).when(taskRepository).deleteById(2);

        // scenarios:
        //      1. delete non existing id
        //      2. delete existing id for diff owner
        //      3. delete existing id for correct owner - best scenario

        // scenario 1
        // act and assert
        assertThrows(TaskNotFoundException.class, () ->
                taskCrudManager.delete(4, "ppires"));

        // scenario 2
        // act and assert
        assertThrows(TaskNotFoundException.class, () ->
                taskCrudManager.delete(2, "ppires"));

        // scenario 3
        // act
        taskCrudManager.delete(2, "dcosta");

        // assert
        assertEquals(fakeSimpleStorage, fakeSimpleStorage.stream()
                .filter(it -> it.getId() != 2)
                .collect(Collectors.toList()));
    }

    @Test
    void complete() {
        // arrange
        when(taskRepository.findById(2))
                .thenReturn(Optional.of(
                        fakeSimpleStorage.stream()
                                .filter(it -> it.getId() == 2)
                                .collect(Collectors.toList())
                                .get(0)));

        when(taskRepository.findById(4))
                .thenReturn(Optional.empty());

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        // scenarios:
        //      1. complete non existing id
        //      2. complete existing id for diff owner
        //      3. complete existing id for correct owner - best scenario

        // scenario 1
        // act and assert
        assertThrows(TaskNotFoundException.class, () ->
                taskCrudManager.complete(4, "ppires"));

        // scenario 2
        // act and assert
        assertThrows(TaskNotFoundException.class, () ->
                taskCrudManager.complete(2, "ppires"));

        // scenario 3
        // act
        assertFalse(fakeSimpleStorage.get(1).isDone());
        taskCrudManager.complete(2, "dcosta");

        // assert
        assertTrue(fakeSimpleStorage.get(1).isDone());
    }
}
