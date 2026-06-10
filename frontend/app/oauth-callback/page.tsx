"use client";

import { useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";

export default function OAuthCallback() {
  const router = useRouter();
  const params = useSearchParams();

  useEffect(() => {
    const token = params.get("token");
    const refresh = params.get("refresh");

    if (token && refresh) {
      localStorage.setItem("accessToken", token);
      localStorage.setItem("refreshToken", refresh);
      router.push("/");
    } else {
      router.push("/login");
    }
  }, []);

  return (
    <div className="min-h-screen flex items-center justify-center">
      <p className="text-sm text-gray-500">Signing you in...</p>
    </div>
  );
}