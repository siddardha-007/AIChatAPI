import { useState } from "react";
import "./App.css";

function App() {
  const [request, setRequest] = useState("");
  const [answer, setAnswer] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const generateAnswer = async () => {
    if (!request.trim()) {
      return;
    }

    setLoading(true);
    setError("");
    setAnswer(null);

    try {
      const response = await fetch(
        "http://localhost:8080/api/ai/generate",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            request: request,
          }),
        }
      );

      if (!response.ok) {
        throw new Error("Failed to generate answer");
      }

      const data = await response.json();

      setAnswer(data);
    } catch (error) {
      console.error(error);
      setError("Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="app">
      <div className="container">
        <h1>AIChatAPI</h1>

        <p className="subtitle">
          Your AI technical assistant
        </p>

        <div className="input-section">
          <label htmlFor="request">
            Ask a technical question
          </label>

          <textarea
            id="request"
            placeholder="Example: What is dependency injection in Spring Boot?"
            value={request}
            onChange={(e) => setRequest(e.target.value)}
          />

          <button
            onClick={generateAnswer}
            disabled={loading}
          >
            {loading ? "Generating..." : "Generate Answer"}
          </button>
        </div>

        {error && (
          <div className="error">
            {error}
          </div>
        )}

        {answer && (
          <div className="answer">
            <h2>{answer.topic}</h2>

            <h3>Explanation</h3>
            <p>{answer.explanation}</p>

            <h3>Example</h3>
            <p>{answer.example}</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default App;