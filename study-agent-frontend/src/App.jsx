import { useState } from "react";
import "./App.css";
import Login from "./Login";


function App() {

  const [loggedIn, setLoggedIn] =
  useState(!!localStorage.getItem("token"));

  const [question, setQuestion] = useState("");
  const [messages, setMessages] = useState([]);

  const [file, setFile] = useState(null);
  const [uploadMessage, setUploadMessage] = useState("");

  const [pageNumber, setPageNumber] = useState("");
  const [generatedQuestions, setGeneratedQuestions] = useState("");

  const askQuestion = async () => {

    if (!question.trim()) {
      return;
    }

    const userMessage = {
      type: "user",
      text: question
    };

    setMessages((prev) => [...prev, userMessage]);

    try {

      const token = localStorage.getItem("token");

      const response = await fetch(
        "http://localhost:8080/api/ask",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: JSON.stringify({
            question: currentQuestion
          })
        }
      );

      const answer = await response.text();

      const aiMessage = {
        type: "ai",
        text: answer
      };

      setMessages((prev) => [...prev, aiMessage]);

    } catch (error) {

      setMessages((prev) => [
        ...prev,
        {
          type: "ai",
          text: "Something went wrong while contacting the server."
        }
      ]);

    }

    setQuestion("");
  };

  const uploadPdf = async () => {

  if (!file) {
    setUploadMessage("Please select a PDF.");
    return;
  }

  const formData = new FormData();
  formData.append("file", file);

  try {

    const token = localStorage.getItem("token");

    const response = await fetch(
      "http://localhost:8080/api/documents/upload",
      {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${token}`
        },
        body: formData
      }
    );

    if (response.ok) {
      setUploadMessage("PDF uploaded successfully.");
    } else {
      setUploadMessage("PDF upload failed.");
    }

  } catch (error) {
    setUploadMessage("Could not connect to server.");
  }
  };

  const generateQuestions = async () => {

  if (!pageNumber) {
    return;
  }

  try {

    const token = localStorage.getItem("token");

    const response = await fetch(
      "http://localhost:8080/api/ask/generate-questions",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({
          pageNumber: Number(pageNumber)
        })
      }
    );

    const result = await response.text();

    setGeneratedQuestions(result);

  } catch (error) {

    setGeneratedQuestions(
      "Something went wrong while generating questions."
    );
  }
  };

  const logout = () => {
  localStorage.removeItem("token");
  setLoggedIn(false);
};

  if (!loggedIn) {
  return <Login onLogin={() => setLoggedIn(true)} />;
  }

  return (
    <div className="app">

      <h1>My Study Agent</h1>

      <button className="logout-button" onClick={logout}>
        Logout
      </button>

      <div className="upload-container">

  <input
    type="file"
    accept=".pdf"
    onChange={(e) => setFile(e.target.files[0])}
  />

  <button onClick={uploadPdf}>
    Upload PDF
  </button>

  <p>{uploadMessage}</p>

      </div>

      <div className="chat-container">

        {messages.map((message, index) => (

          <div
            key={index}
            className={`message ${message.type}`}
          >
            {message.text}
          </div>

        ))}

      </div>

      <div className="input-container">

        <input
          type="text"
          placeholder="Ask a question..."
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") {
              askQuestion();
            }
          }}
        />

        <button onClick={askQuestion}>
          Send
        </button>

      </div>

      <div className="question-generator">

  <h2>Generate Questions</h2>

  <div className="question-input">

    <input
      type="number"
      placeholder="Page number"
      value={pageNumber}
      onChange={(e) => setPageNumber(e.target.value)}
    />

    <button onClick={generateQuestions}>
      Generate
    </button>

  </div>

  {generatedQuestions && (
    <div className="generated-questions">
      <h3>Questions</h3>
      <p>{generatedQuestions}</p>
    </div>
  )}

      </div>

    </div>



  );
}

export default App;