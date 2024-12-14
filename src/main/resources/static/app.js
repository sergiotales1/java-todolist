fetch("/api/todos")
  .then((response) => response.json())
  .then((todos) => {
    const todoTable = document.querySelector(".todo-table tbody");
    todos.forEach((todo) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
        <td><button class="delete-btn" onclick="deleteTodo(${
          todo.id
        })">❌</button></td>
        <td>${todo.id}</td>
        <td><span class="description"><a class="app-link" href="edit.html/${
          todo.id
        }">${todo.description}</a></span></td>
        <td>
        <a class="app-link" href="edit.html/${todo.id}">
        <div class="${todo.isDone ? "checkbox done" : "checkbox"}"></div>
        </a>
        </td>
      `;
      todoTable.appendChild(tr);
    });
  })
  .catch((error) => {
    console.error("Error fetching todos:", error);
  });

async function deleteTodo(id) {
  const todoData = {
    id,
  };

  try {
    const response = await fetch("/api/todo", {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(todoData),
    });

    if (response.ok) {
      const { id } = await response.json();
      alert("Todo deleted successfully! ID: " + id);
      window.location.reload();
    } else {
      alert("Error to delete the todo");
    }
  } catch (error) {
    console.error("Error:", error);
    alert("An error occurred while edtion of the todo.");
  }
}
