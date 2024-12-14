const currentPath = window.location.pathname;

const pathParts = currentPath.split("/");
const id = pathParts[pathParts.length - 1];

const fetchTodo = async (id) => {
  try {
    const response = await fetch(`/api/todo/${id}`);

    if (!response.ok) {
      throw new Error(`Failed to fetch todo: ${response.statusText}`);
    }

    const todo = await response.json();

    document.querySelector(".edit-title").innerHTML = `Edit todo: ${todo.id}`;
    document.querySelector("#description").value = todo.description;
    document.querySelector("#isDone").checked = todo.isDone;
  } catch (error) {
    console.error("Error fetching todo:", error);
    alert("An error occurred while fetching the todo item.");
  }
};

fetchTodo(id);

document
  .getElementById("todo-form")
  .addEventListener("submit", async (event) => {
    event.preventDefault();
    const path = window.location.pathname;
    const id = path.split("/")[2];
    const parsedId = parseInt(id);

    const description = document.getElementById("description").value;
    const isDone = document.getElementById("isDone").checked;

    const todoData = {
      description,
      isDone,
      parsedId,
    };

    try {
      const response = await fetch("/api/todo", {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(todoData),
      });
      console.log(response);

      if (response.ok) {
        const { id } = await response.json();
        alert("Todo edited successfully! ID: " + id);
        window.location.href = "/";
      } else {
        alert("Error to edit the todo");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("An error occurred while edtion of the todo.");
    }
  });
