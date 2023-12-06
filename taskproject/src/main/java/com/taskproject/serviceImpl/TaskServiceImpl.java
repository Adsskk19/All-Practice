package com.taskproject.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taskproject.entity.Task;
import com.taskproject.entity.Users;
import com.taskproject.exception.APIException;
import com.taskproject.exception.TaskNotFound;
import com.taskproject.exception.UserNotFound;
import com.taskproject.payload.TaskDto;
import com.taskproject.repository.TaskRepository;
import com.taskproject.repository.UserRepository;
import com.taskproject.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	
	
	@Override
	public TaskDto saveTask(long userid, TaskDto taskDto) {
		
		Users user = userRepository.findById(userid).orElseThrow(
				()-> new UserNotFound(String.format("User Id %d not found",userid))
				);
		Task task = modelMapper.map(taskDto, Task.class);
		task.setUsers(user);
		//After setting the user,we are storing the data in DB
		Task savedtask = taskRepository.save(task);
		return modelMapper.map(savedtask, TaskDto.class);
	}

	@Override
	public List<TaskDto> getAllTasks(long userid) {
		 userRepository.findById(userid).orElseThrow(
				()-> new UserNotFound(String.format("User Id %d not found",userid))
				);	
		 List<Task> tasks = taskRepository.findAllByUsersId(userid);
		 
		 return tasks.stream().map(
				 task -> modelMapper.map(tasks,  TaskDto.class)
				 ).collect(Collectors.toList());
	}

	@Override
	public TaskDto getTask(long userid, long taskid) {
		Users users = userRepository.findById(userid).orElseThrow(
				()-> new UserNotFound(String.format("User Id %d not found",userid))
				);	
		Task task = taskRepository.findById(taskid).orElseThrow(
				()-> new TaskNotFound(String.format("Task Id %d not found",taskid))
				);
		if(users.getId() != task.getUsers().getId()) {
			throw new APIException(String.format("Task Id %d not belongs to User Id %d", taskid,userid));
		}		
		return modelMapper.map(task, TaskDto.class);
	}

	@Override
	public void deleteTask(long userid, long taskid) {
		Users users = userRepository.findById(userid).orElseThrow(
				()-> new UserNotFound(String.format("User Id %d not found",userid))
				);	
		Task task = taskRepository.findById(taskid).orElseThrow(
				()-> new TaskNotFound(String.format("Task Id %d not found",taskid))
				);
		if(users.getId() != task.getUsers().getId()) {
			throw new APIException(String.format("Task Id %d not belongs to User Id %d", taskid,userid));
		}
	}

}
