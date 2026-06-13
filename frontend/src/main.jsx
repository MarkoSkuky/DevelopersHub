import React, { useEffect, useState } from "react";
import { createRoot } from "react-dom/client";
import "./styles.css";

const API_BASE_URL = "/api";

function App() {
  const [developers, setDevelopers] = useState([]);
  const [projects, setProjects] = useState([]);

  const [error, setError] = useState("");
  const [info, setInfo] = useState("");

  const [developerForm, setDeveloperForm] = useState({
    name: "",
    email: "",
    seniority: "JUNIOR",
    skills: "",
    salaryExpectation: ""
  });

  const [projectForm, setProjectForm] = useState({
    name: "",
    description: "",
    requiredSkills: "",
    active: true
  });

  async function api(path, options = {}) {
    setError("");
    setInfo("");

    const response = await fetch(`${API_BASE_URL}${path}`, {
      headers: {
        "Content-Type": "application/json",
        ...(options.headers || {})
      },
      ...options
    });

    const contentType = response.headers.get("content-type");

    const data =
        contentType && contentType.includes("application/json")
            ? await response.json()
            : await response.text();

    if (!response.ok) {
      const message =
          typeof data === "string"
              ? data
              : data.message || data.error || JSON.stringify(data);

      throw new Error(`${response.status} ${response.statusText}: ${message}`);
    }

    return data;
  }

  async function loadAll() {
    try {
      const [devs, projs] = await Promise.all([
        api("/developers"),
        api("/projects")
      ]);

      setDevelopers(devs);
      setProjects(projs);
    } catch (e) {
      setError(e.message);
    }
  }

  useEffect(() => {
    loadAll();
  }, []);

  async function createDeveloper(e) {
    e.preventDefault();

    const payload = {
      name: developerForm.name,
      email: developerForm.email,
      seniority: developerForm.seniority,
      skills: developerForm.skills
          .split(",")
          .map(s => s.trim())
          .filter(Boolean),
      salaryExpectation: Number(developerForm.salaryExpectation)
    };

    try {
      await api("/developers", {
        method: "POST",
        body: JSON.stringify(payload)
      });

      setDeveloperForm({
        name: "",
        email: "",
        seniority: "JUNIOR",
        skills: "",
        salaryExpectation: ""
      });

      setInfo("Developer created");
      await loadAll();
    } catch (e) {
      setError(e.message);
    }
  }

  async function createProject(e) {
    e.preventDefault();

    const payload = {
      name: projectForm.name,
      description: projectForm.description,
      requiredSkills: projectForm.requiredSkills
          .split(",")
          .map(s => s.trim())
          .filter(Boolean),
      active: projectForm.active
    };

    try {
      await api("/projects", {
        method: "POST",
        body: JSON.stringify(payload)
      });

      setProjectForm({
        name: "",
        description: "",
        requiredSkills: "",
        active: true
      });

      setInfo("Project created");
      await loadAll();
    } catch (e) {
      setError(e.message);
    }
  }

  async function deleteDeveloper(id) {
    try {
      await api(`/developers/${id}`, {
        method: "DELETE"
      });

      setInfo("Developer deleted");
      await loadAll();
    } catch (e) {
      setError(e.message);
    }
  }

  async function trigger404() {
    try {
      await api("/developers/999999");
    } catch (e) {
      setError(e.message);
    }
  }

  async function trigger400() {
    try {
      await api("/developers", {
        method: "POST",
        body: JSON.stringify({
          name: "",
          email: "bad-email",
          seniority: "JUNIOR",
          skills: [],
          salaryExpectation: -10
        })
      });
    } catch (e) {
      setError(e.message);
    }
  }

  return (
      <main className="page">
        <header>
          <h1>DeveloperHub</h1>
          <p>
            Skuska123123123 hahaha dusan vlk
          </p>
        </header>

        {error && <div className="alert error">{error}</div>}
        {info && <div className="alert info">{info}</div>}

        <section className="grid">

          <form className="card" onSubmit={createDeveloper}>
            <h2>Create Developer</h2>

            <label>Name</label>
            <input
                value={developerForm.name}
                onChange={e =>
                    setDeveloperForm({
                      ...developerForm,
                      name: e.target.value
                    })
                }
            />

            <label>Email</label>
            <input
                value={developerForm.email}
                onChange={e =>
                    setDeveloperForm({
                      ...developerForm,
                      email: e.target.value
                    })
                }
            />

            <label>Seniority</label>
            <select
                value={developerForm.seniority}
                onChange={e =>
                    setDeveloperForm({
                      ...developerForm,
                      seniority: e.target.value
                    })
                }
            >
              <option>JUNIOR</option>
              <option>MEDIOR</option>
              <option>SENIOR</option>
            </select>

            <label>Skills comma separated</label>
            <input
                value={developerForm.skills}
                onChange={e =>
                    setDeveloperForm({
                      ...developerForm,
                      skills: e.target.value
                    })
                }
                placeholder="java, spring, aws"
            />

            <label>Salary expectation</label>
            <input
                type="number"
                value={developerForm.salaryExpectation}
                onChange={e =>
                    setDeveloperForm({
                      ...developerForm,
                      salaryExpectation: e.target.value
                    })
                }
            />

            <button type="submit">
              Create developer
            </button>
          </form>

          <form className="card" onSubmit={createProject}>
            <h2>Create Project</h2>

            <label>Name</label>
            <input
                value={projectForm.name}
                onChange={e =>
                    setProjectForm({
                      ...projectForm,
                      name: e.target.value
                    })
                }
            />

            <label>Description</label>
            <textarea
                value={projectForm.description}
                onChange={e =>
                    setProjectForm({
                      ...projectForm,
                      description: e.target.value
                    })
                }
            />

            <label>Required skills</label>
            <input
                value={projectForm.requiredSkills}
                onChange={e =>
                    setProjectForm({
                      ...projectForm,
                      requiredSkills: e.target.value
                    })
                }
                placeholder="java, kafka"
            />

            <label className="checkbox">
              <input
                  type="checkbox"
                  checked={projectForm.active}
                  onChange={e =>
                      setProjectForm({
                        ...projectForm,
                        active: e.target.checked
                      })
                  }
              />
              Active
            </label>

            <button type="submit">
              Create project
            </button>
          </form>
        </section>

        <section className="card">
          <h2>Developers</h2>

          <button onClick={loadAll}>
            Reload
          </button>

          <div className="list">
            {developers.map(dev => (
                <div className="row" key={dev.id}>
                  <div>
                    <strong>{dev.name}</strong>
                    <span>{dev.email}</span>

                    <small>
                      {dev.seniority} ·{" "}
                      {dev.skills?.join(", ")} ·{" "}
                      {dev.salaryExpectation} €
                    </small>
                  </div>

                  <button
                      className="danger"
                      onClick={() => deleteDeveloper(dev.id)}
                  >
                    Delete
                  </button>
                </div>
            ))}
          </div>
        </section>

        <section className="card">
          <h2>Projects</h2>

          <div className="list">
            {projects.map(project => (
                <div className="row" key={project.id}>
                  <div>
                    <strong>{project.name}</strong>

                    <span>{project.description}</span>

                    <small>
                      Skills: {project.requiredSkills?.join(", ")} ·
                      Active: {String(project.active)}
                    </small>
                  </div>
                </div>
            ))}
          </div>
        </section>

        <section className="card">
          <h2>Error handling playground</h2>

          <p>
            Tu testuješ 400 a 404 z backendu.
          </p>

          <div className="inline">
            <button onClick={trigger400}>
              Trigger 400
            </button>

            <button onClick={trigger404}>
              Trigger 404
            </button>
          </div>
        </section>
      </main>
  );
}

createRoot(document.getElementById("root")).render(<App />);

// import React, { useEffect, useState } from "react";
// import { createRoot } from "react-dom/client";
// import "./styles.css";
//
// const API_BASE_URL = "http://localhost:8080";
//
// function App() {
//   const [developers, setDevelopers] = useState([]);
//   const [projects, setProjects] = useState([]);
//   const [applications, setApplications] = useState([]);
//   const [error, setError] = useState("");
//   const [info, setInfo] = useState("");
//
//   const [developerForm, setDeveloperForm] = useState({
//     name: "",
//     email: "",
//     seniority: "JUNIOR",
//     skills: "",
//     salaryExpectation: ""
//   });
//
//   const [projectForm, setProjectForm] = useState({
//     name: "",
//     description: "",
//     requiredSkills: "",
//     active: true
//   });
//
//   async function api(path, options = {}) {
//     setError("");
//     setInfo("");
//
//     const response = await fetch(`${API_BASE_URL}${path}`, {
//       headers: {
//         "Content-Type": "application/json",
//         ...(options.headers || {})
//       },
//       ...options
//     });
//
//     const contentType = response.headers.get("content-type");
//     const data = contentType && contentType.includes("application/json")
//       ? await response.json()
//       : await response.text();
//
//     if (!response.ok) {
//       const message =
//         typeof data === "string"
//           ? data
//           : data.message || data.error || JSON.stringify(data);
//
//       throw new Error(`${response.status} ${response.statusText}: ${message}`);
//     }
//
//     return data;
//   }
//
//   async function loadAll() {
//     try {
//       const [devs, projs, apps] = await Promise.all([
//         api("/developers"),
//         api("/projects"),
//         api("/applications")
//       ]);
//
//       setDevelopers(devs);
//       setProjects(projs);
//       setApplications(apps);
//     } catch (e) {
//       setError(e.message);
//     }
//   }
//
//   useEffect(() => {
//     loadAll();
//   }, []);
//
//   async function createDeveloper(e) {
//     e.preventDefault();
//
//     const payload = {
//       name: developerForm.name,
//       email: developerForm.email,
//       seniority: developerForm.seniority,
//       skills: developerForm.skills
//         .split(",")
//         .map(s => s.trim())
//         .filter(Boolean),
//       salaryExpectation: Number(developerForm.salaryExpectation)
//     };
//
//     try {
//       await api("/developers", {
//         method: "POST",
//         body: JSON.stringify(payload)
//       });
//
//       setDeveloperForm({
//         name: "",
//         email: "",
//         seniority: "JUNIOR",
//         skills: "",
//         salaryExpectation: ""
//       });
//
//       setInfo("Developer created");
//       await loadAll();
//     } catch (e) {
//       setError(e.message);
//     }
//   }
//
//   async function createProject(e) {
//     e.preventDefault();
//
//     const payload = {
//       name: projectForm.name,
//       description: projectForm.description,
//       requiredSkills: projectForm.requiredSkills
//         .split(",")
//         .map(s => s.trim())
//         .filter(Boolean),
//       active: projectForm.active
//     };
//
//     try {
//       await api("/projects", {
//         method: "POST",
//         body: JSON.stringify(payload)
//       });
//
//       setProjectForm({
//         name: "",
//         description: "",
//         requiredSkills: "",
//         active: true
//       });
//
//       setInfo("Project created");
//       await loadAll();
//     } catch (e) {
//       setError(e.message);
//     }
//   }
//
//   async function applyToProject(developerId, projectId) {
//     try {
//       await api("/applications", {
//         method: "POST",
//         body: JSON.stringify({ developerId, projectId })
//       });
//
//       setInfo("Application created");
//       await loadAll();
//     } catch (e) {
//       setError(e.message);
//     }
//   }
//
//   async function updateApplicationStatus(applicationId, status) {
//     try {
//       await api(`/applications/${applicationId}/status`, {
//         method: "PATCH",
//         body: JSON.stringify({ status })
//       });
//
//       setInfo(`Application status changed to ${status}`);
//       await loadAll();
//     } catch (e) {
//       setError(e.message);
//     }
//   }
//
//   async function deleteDeveloper(id) {
//     try {
//       await api(`/developers/${id}`, { method: "DELETE" });
//       setInfo("Developer deleted");
//       await loadAll();
//     } catch (e) {
//       setError(e.message);
//     }
//   }
//
//   async function trigger404() {
//     try {
//       await api("/developers/999999");
//     } catch (e) {
//       setError(e.message);
//     }
//   }
//
//   async function trigger400() {
//     try {
//       await api("/developers", {
//         method: "POST",
//         body: JSON.stringify({
//           name: "",
//           email: "bad-email",
//           seniority: "JUNIOR",
//           skills: [],
//           salaryExpectation: -10
//         })
//       });
//     } catch (e) {
//       setError(e.message);
//     }
//   }
//
//   return (
//     <main className="page">
//       <header>
//         <h1>DeveloperHub</h1>
//         <p>Frontend na tréning Spring Boot REST API, DTO, validation, error handling a testov.</p>
//       </header>
//
//       {error && <div className="alert error">{error}</div>}
//       {info && <div className="alert info">{info}</div>}
//
//       <section className="grid">
//         <form className="card" onSubmit={createDeveloper}>
//           <h2>Create Developer</h2>
//
//           <label>Name</label>
//           <input value={developerForm.name} onChange={e => setDeveloperForm({...developerForm, name: e.target.value})} />
//
//           <label>Email</label>
//           <input value={developerForm.email} onChange={e => setDeveloperForm({...developerForm, email: e.target.value})} />
//
//           <label>Seniority</label>
//           <select value={developerForm.seniority} onChange={e => setDeveloperForm({...developerForm, seniority: e.target.value})}>
//             <option>JUNIOR</option>
//             <option>MEDIOR</option>
//             <option>SENIOR</option>
//           </select>
//
//           <label>Skills comma separated</label>
//           <input value={developerForm.skills} onChange={e => setDeveloperForm({...developerForm, skills: e.target.value})} placeholder="java, spring, aws" />
//
//           <label>Salary expectation</label>
//           <input type="number" value={developerForm.salaryExpectation} onChange={e => setDeveloperForm({...developerForm, salaryExpectation: e.target.value})} />
//
//           <button type="submit">Create developer</button>
//         </form>
//
//         <form className="card" onSubmit={createProject}>
//           <h2>Create Project</h2>
//
//           <label>Name</label>
//           <input value={projectForm.name} onChange={e => setProjectForm({...projectForm, name: e.target.value})} />
//
//           <label>Description</label>
//           <textarea value={projectForm.description} onChange={e => setProjectForm({...projectForm, description: e.target.value})} />
//
//           <label>Required skills comma separated</label>
//           <input value={projectForm.requiredSkills} onChange={e => setProjectForm({...projectForm, requiredSkills: e.target.value})} placeholder="java, kafka" />
//
//           <label className="checkbox">
//             <input type="checkbox" checked={projectForm.active} onChange={e => setProjectForm({...projectForm, active: e.target.checked})} />
//             Active
//           </label>
//
//           <button type="submit">Create project</button>
//         </form>
//       </section>
//
//       <section className="card">
//         <h2>Developers</h2>
//         <div className="list">
//           {developers.map(dev => (
//             <div className="row" key={dev.id}>
//               <div>
//                 <strong>{dev.name}</strong>
//                 <span>{dev.email}</span>
//                 <small>{dev.seniority} · {dev.skills?.join(", ")} · {dev.salaryExpectation} €</small>
//               </div>
//               <button className="danger" onClick={() => deleteDeveloper(dev.id)}>Delete</button>
//             </div>
//           ))}
//         </div>
//       </section>
//
//       <section className="card">
//         <h2>Projects</h2>
//         <div className="list">
//           {projects.map(project => (
//             <div className="row" key={project.id}>
//               <div>
//                 <strong>{project.name}</strong>
//                 <span>{project.description}</span>
//                 <small>Skills: {project.requiredSkills?.join(", ")} · Active: {String(project.active)}</small>
//               </div>
//
//               <div className="inline">
//                 {developers.map(dev => (
//                   <button key={dev.id} onClick={() => applyToProject(dev.id, project.id)}>
//                     Apply {dev.name}
//                   </button>
//                 ))}
//               </div>
//             </div>
//           ))}
//         </div>
//       </section>
//
//       <section className="card">
//         <h2>Applications</h2>
//         <div className="list">
//           {applications.map(app => (
//             <div className="row" key={app.id}>
//               <div>
//                 <strong>Application #{app.id}</strong>
//                 <span>Developer ID: {app.developerId} · Project ID: {app.projectId}</span>
//                 <small>Status: {app.status} · Created: {app.createdAt}</small>
//               </div>
//
//               <div className="inline">
//                 <button onClick={() => updateApplicationStatus(app.id, "APPROVED")}>Approve</button>
//                 <button onClick={() => updateApplicationStatus(app.id, "REJECTED")}>Reject</button>
//               </div>
//             </div>
//           ))}
//         </div>
//       </section>
//
//       <section className="card">
//         <h2>Error handling playground</h2>
//         <p>Tu budeš testovať 400 a 404 z backendu.</p>
//         <div className="inline">
//           <button onClick={trigger400}>Trigger 400</button>
//           <button onClick={trigger404}>Trigger 404</button>
//         </div>
//       </section>
//     </main>
//   );
// }
//
// createRoot(document.getElementById("root")).render(<App />);
