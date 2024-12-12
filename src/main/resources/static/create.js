document.getElementById("todo-form").addEventListener("submit", async (event) => {
    event.preventDefault();

    const description = document.getElementById("description").value;

    const todoData = {
        description,
    };

    try {
        const response = await fetch("/api/todos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(todoData),
        });
        console.log(response)

        if (response.ok) {
            const {id} = await response.json();
            alert("Todo added successfully! ID: " + id);
            window.location.href = "/";
        } else {
            alert("Error adding todo");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("An error occurred while adding the todo.");
    }
});
