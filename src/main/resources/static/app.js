console.log("JavaScript loaded successfully!");

fetch('/api/todos')
  .then(response => response.json())
  .then(todos => {
    const todoTable = document.querySelector('.todo-table tbody');
    todos.forEach(todo => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td><button class="delete-btn">❌</button></td>
        <td>${todo.id}</td>
        <td><span class="description"><a class="app-link" href="edit.html/${todo.id}">${todo.description}</a></span></td>
        <td><input type="checkbox" ${todo.isDone ? 'checked' : ''}></td>
      `;
      todoTable.appendChild(tr);
    });
  })
  .catch(error => {
    console.error('Error fetching todos:', error);
  });

