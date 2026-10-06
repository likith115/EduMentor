import { useState } from "react";

function Login({ onLogin }) {

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isRegister, setIsRegister] = useState(false);
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    const endpoint = isRegister
      ? "register"
      : "login";

    const response = await fetch(
      `http://localhost:8080/api/auth/${endpoint}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          email,
          password
        })
      }
    );

    const data = await response.text();

    if (!response.ok) {
      setMessage(data);
      return;
    }

    if (isRegister) {
      setMessage("Registration successful. Please login.");
      setIsRegister(false);
    } else {
      localStorage.setItem("token", data);
      onLogin();
    }
  };

  return (
    <main className="auth-page">
      <section className="auth-card">
        <div className="auth-heading">
          <p className="auth-eyebrow">My Study Agent</p>
          <h2>{isRegister ? "Create your account" : "Welcome back"}</h2>
          <p className="auth-description">
            {isRegister
              ? "Sign up to start studying with your AI assistant."
              : "Log in to continue your study session."}
          </p>
        </div>

        <form className="auth-form" onSubmit={handleSubmit}>
          <label htmlFor="auth-email">Email</label>
          <input
            id="auth-email"
            type="email"
            placeholder="you@example.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            autoComplete="email"
            required
          />

          <label htmlFor="auth-password">Password</label>
          <input
            id="auth-password"
            type="password"
            placeholder="Enter your password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            autoComplete={isRegister ? "new-password" : "current-password"}
            required
          />

          <button className="auth-submit" type="submit">
            {isRegister ? "Create account" : "Log in"}
          </button>
        </form>

        {message && <p className="auth-message" role="status">{message}</p>}

        <p className="auth-switch">
          {isRegister ? "Already have an account?" : "New to Study Agent?"}
          <button
            className="auth-switch-button"
            type="button"
            onClick={() => setIsRegister(!isRegister)}
          >
            {isRegister ? "Log in" : "Create an account"}
          </button>
        </p>
      </section>
    </main>
  );
}

export default Login;