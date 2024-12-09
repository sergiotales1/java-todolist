console.log("JavaScript loaded successfully!");

fetch('/api/todos')
  .then(response => response.json())
  .then(todos => {
    // Populate the todo table with the fetched data
    const todoTable = document.querySelector('.todo-table tbody');
    todos.forEach(todo => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td><button class="delete-btn">❌</button></td>
        <td>${todo.id}</td>
        <td><span class="description">${todo.description}</span></td>
        <td><input type="checkbox" ${todo.done ? 'checked' : ''}></td>
      `;
      todoTable.appendChild(tr);
    });
  })
  .catch(error => {
    console.error('Error fetching todos:', error);
  });
