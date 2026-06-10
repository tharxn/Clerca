"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { loginUser, registerUser } from "@/lib/api";

export default function LoginPage() {
  const router = useRouter();

  const [isRegister, setIsRegister] = useState(false);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit() {
    setError("");
    setLoading(true);

    try {
      const data = isRegister
        ? await registerUser({ name, email, password })
        : await loginUser({ email, password });

      localStorage.setItem("accessToken", data.accessToken);
      localStorage.setItem("refreshToken", data.refreshToken);
      localStorage.setItem("userName", data.name);
      localStorage.setItem("userEmail", data.email);

      router.push("/");
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("Something went wrong");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-zinc-950">
      <div className="bg-white dark:bg-zinc-900 border border-gray-200 dark:border-zinc-800 rounded-2xl p-8 w-full max-w-sm shadow-sm">
        <h1 className="text-2xl font-semibold mb-1">
          {isRegister ? "Create account" : "Welcome back"}
        </h1>

        <p className="text-sm text-gray-500 dark:text-zinc-400 mb-6">
          {isRegister ? "Sign up to Clerca" : "Sign in to Clerca"}
        </p>

        <div className="flex flex-col gap-3">
          {isRegister && (
            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Name"
              className="w-full px-3 py-2 rounded-xl border border-gray-200 dark:border-zinc-700 bg-transparent text-sm outline-none focus:border-gray-400 dark:focus:border-zinc-500"
            />
          )}

          <input
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
            type="email"
            className="w-full px-3 py-2 rounded-xl border border-gray-200 dark:border-zinc-700 bg-transparent text-sm outline-none focus:border-gray-400 dark:focus:border-zinc-500"
          />

          <input
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
            type="password"
            className="w-full px-3 py-2 rounded-xl border border-gray-200 dark:border-zinc-700 bg-transparent text-sm outline-none focus:border-gray-400 dark:focus:border-zinc-500"
          />

          {error && (
            <p className="text-red-500 text-xs">
              {error}
            </p>
          )}

          <button
            onClick={handleSubmit}
            disabled={loading}
            className="w-full py-2 rounded-xl bg-black dark:bg-white text-white dark:text-black text-sm font-medium disabled:opacity-50"
          >
            {loading
              ? "Please wait..."
              : isRegister
              ? "Sign up"
              : "Sign in"}
          </button>

          <a
            href="http://localhost:8080/oauth2/authorization/google"
            className="w-full py-2 rounded-xl border border-gray-200 dark:border-zinc-700 text-sm text-center hover:bg-gray-50 dark:hover:bg-zinc-800 transition block"
          >
            Continue with Google
          </a>
        </div>

        <p className="text-xs text-center text-gray-500 dark:text-zinc-500 mt-4">
          {isRegister
            ? "Already have an account?"
            : "Don't have an account?"}{" "}
          <button
            onClick={() => setIsRegister(!isRegister)}
            className="underline"
          >
            {isRegister ? "Sign in" : "Sign up"}
          </button>
        </p>
      </div>
    </div>
  );
}