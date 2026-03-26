const BASE_URL = "http://localhost:8081";
const ADMIN_USER = "admin";
const ADMIN_PASSWORD = "admin";

const REALM_NAME = "master";
const USER = {
  username: "luiza",
  email: "luiza@email.com",
  firstName: "Luiza",
  lastName: "Silva",
  password: "123456"
};

async function getAdminToken() {
  const res = await fetch(`${BASE_URL}/realms/master/protocol/openid-connect/token`, {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    body: new URLSearchParams({
      username: ADMIN_USER,
      password: ADMIN_PASSWORD,
      grant_type: "password",
      client_id: "admin-cli"
    })
  });

  const data = await res.json();
  return data.access_token;
}

async function createRealm(token) {
  const res = await fetch(`${BASE_URL}/admin/realms`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      realm: REALM_NAME,
      enabled: true
    })
  });

  if (res.status === 201) {
    console.log("✅ Realm criado");
  } else if (res.status === 409) {
    console.log("⚠️ Realm já existe");
  } else {
    console.log("❌ Erro ao criar realm", await res.text());
  }
}

async function createUser(token) {
  const res = await fetch(`${BASE_URL}/admin/realms/${REALM_NAME}/users`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      username: USER.username,
      email: USER.email,
      firstName: USER.firstName,
      lastName: USER.lastName,
      enabled: true
    })
  });

  if (res.status === 201) {
    console.log("✅ Usuário criado");
  } else if (res.status === 409) {
    console.log("⚠️ Usuário já existe");
  } else {
    console.log("❌ Erro ao criar usuário", await res.text());
  }
}


async function getUserId(token) {
  const res = await fetch(
    `${BASE_URL}/admin/realms/${REALM_NAME}/users?username=${USER.username}`,
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  );

  const users = await res.json();
  return users[0]?.id;
}


async function setPassword(token, userId) {
  const res = await fetch(
    `${BASE_URL}/admin/realms/${REALM_NAME}/users/${userId}/reset-password`,
    {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        type: "password",
        value: USER.password,
        temporary: false
      })
    }
  );

  if (res.status === 204) {
    console.log("🔑 Senha definida");
  } else {
    console.log("❌ Erro ao definir senha", await res.text());
  }
}

async function run() {
  try {
    const token = await getAdminToken();
    await createRealm(token);
    await createUser(token);

    const userId = await getUserId(token);
    if (!userId) {
      throw new Error("Usuário não encontrado");
    }

    await setPassword(token, userId);

    console.log("🎉 Tudo pronto!");
  } catch (err) {
    console.error("Erro geral:", err);
  }
}

run();