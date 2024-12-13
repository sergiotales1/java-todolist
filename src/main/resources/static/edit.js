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
    console.error('Error fetching todo:', error);
    alert('An error occurred while fetching the todo item.');
  }
};


fetchTodo(id)