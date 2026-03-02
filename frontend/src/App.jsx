import { useEffect, useState } from 'react';

function App() {
  const [spaceships, setSpaceships] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const [form, setForm] = useState({
    name: '',
    classType: 'FIGHTER',
    crewCapacity: 0,
    currentCrewCount: 0,
    homeBase: '',
    fuelLevelPercent: 0
  });
  const [saving, setSaving] = useState(false);
  const [editingId, setEditingId] = useState(null);

  const [requestsCount, setRequestsCount] = useState(null);

  const authHeader = 'Basic ' + btoa('admin:admin');

  useEffect(() => {
    const fetchSpaceships = async () => {
      setLoading(true);
      setError('');
      try {
        const response = await fetch(
          'http://localhost:8080/api/spaceships?page=0&size=20',
          {
            headers: {
              'Content-Type': 'application/json',
              Authorization: authHeader
            },
            credentials: 'include'
          }
        );
        if (!response.ok) {
          throw new Error(`HTTP error ${response.status}`);
        }
        const data = await response.json();
        setSpaceships(data.content ?? data);
      } catch (e) {
        setError(e.message);
      } finally {
        setLoading(false);
      }
    };

    const fetchRequestsCount = async () => {
      try {
        const response = await fetch(
          'http://localhost:8080/api/metrics/requests-count',
          {
            headers: {
              Authorization: authHeader
            },
            credentials: 'include'
          }
        );
        if (!response.ok) {
          throw new Error(`HTTP error ${response.status}`);
        }
        const text = await response.text();
        setRequestsCount(Number(text));
      } catch (e) {
        console.error(e);
      }
    };

    fetchSpaceships();
    fetchRequestsCount();
  }, [authHeader]);

  const refreshRequestsCount = async () => {
    try {
      const response = await fetch(
        'http://localhost:8080/api/metrics/requests-count',
        {
          headers: {
            Authorization: authHeader
          },
          credentials: 'include'
        }
      );
      if (!response.ok) {
        throw new Error(`HTTP error ${response.status}`);
      }
      const text = await response.text();
      setRequestsCount(Number(text));
    } catch (e) {
      setError(e.message);
    }
  };

  return (
    <div style={{ padding: '20px', background: '#222', minHeight: '100vh', color: '#eee' }}>
      <h1>Spaceship Fleet UI</h1>

      <p>
        Total requests (backend counter):{' '}
        {requestsCount === null ? '...' : requestsCount}
      </p>
      <button
        onClick={refreshRequestsCount}
        style={{ marginBottom: '15px' }}
      >
        Refresh requests count
      </button>

      {loading && <p>Loading...</p>}
      {error && <p style={{ color: 'red' }}>Error: {error}</p>}

      <table
        border="1"
        cellPadding="6"
        style={{
          borderCollapse: 'collapse',
          marginTop: '10px',
          background: '#333',
          color: '#eee'
        }}
      >
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Class</th>
            <th>Crew</th>
            <th>Home base</th>
            <th>Fuel %</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {spaceships.map(s => (
            <tr key={s.id}>
              <td>{s.id}</td>
              <td>{s.name}</td>
              <td>{s.classType}</td>
              <td>
                {s.currentCrewCount} / {s.crewCapacity}
              </td>
              <td>{s.homeBase}</td>
              <td>{s.fuelLevelPercent}</td>
              <td>
                <button
                  onClick={() => {
                    setEditingId(s.id);
                    setForm({
                      name: s.name,
                      classType: s.classType,
                      crewCapacity: s.crewCapacity,
                      currentCrewCount: s.currentCrewCount,
                      homeBase: s.homeBase,
                      fuelLevelPercent: s.fuelLevelPercent
                    });
                  }}
                  style={{ marginRight: '6px' }}
                >
                  Edit
                </button>
                <button
                  onClick={async () => {
                    if (!window.confirm(`Delete spaceship ${s.name}?`)) return;
                    try {
                      const response = await fetch(
                        `http://localhost:8080/api/spaceships/${s.id}`,
                        {
                          method: 'DELETE',
                          headers: {
                            Authorization: authHeader
                          },
                          credentials: 'include'
                        }
                      );
                      if (!response.ok) {
                        const errText = await response.text();
                        throw new Error(`HTTP ${response.status}: ${errText}`);
                      }
                      setSpaceships(prev => prev.filter(x => x.id !== s.id));
                      refreshRequestsCount();
                    } catch (e) {
                      setError(e.message);
                    }
                  }}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
          {spaceships.length === 0 && !loading && (
            <tr>
              <td colSpan="7" style={{ textAlign: 'center' }}>
                No spaceships yet
              </td>
            </tr>
          )}
        </tbody>
      </table>

      <h2 style={{ marginTop: '30px' }}>
        {editingId ? 'Edit spaceship' : 'Add new spaceship'}
      </h2>

      <form
        onSubmit={async e => {
          e.preventDefault();
          setSaving(true);
          setError('');
          try {
            const isEditing = editingId !== null;
            const url = isEditing
              ? `http://localhost:8080/api/spaceships/${editingId}`
              : 'http://localhost:8080/api/spaceships';
            const method = isEditing ? 'PUT' : 'POST';

            const response = await fetch(url, {
              method,
              headers: {
                'Content-Type': 'application/json',
                Authorization: authHeader
              },
              body: JSON.stringify(form),
              credentials: 'include'
            });
            if (!response.ok) {
              const errText = await response.text();
              throw new Error(`HTTP ${response.status}: ${errText}`);
            }
            const saved = await response.json();

            if (isEditing) {
              setSpaceships(prev =>
                prev.map(x => (x.id === saved.id ? saved : x))
              );
            } else {
              setSpaceships(prev => [saved, ...prev]);
            }

            setForm({
              name: '',
              classType: 'FIGHTER',
              crewCapacity: 0,
              currentCrewCount: 0,
              homeBase: '',
              fuelLevelPercent: 0
            });
            setEditingId(null);
            refreshRequestsCount();
          } catch (e) {
            setError(e.message);
          } finally {
            setSaving(false);
          }
        }}
        style={{
          marginTop: '10px',
          display: 'grid',
          gap: '8px',
          maxWidth: '320px'
        }}
      >
        <label>
          Name:{' '}
          <input
            type="text"
            value={form.name}
            onChange={e => setForm({ ...form, name: e.target.value })}
            required
          />
        </label>

        <label>
          Class:{' '}
          <select
            value={form.classType}
            onChange={e => setForm({ ...form, classType: e.target.value })}
          >
            <option value="FIGHTER">FIGHTER</option>
            <option value="CARGO">CARGO</option>
            <option value="EXPLORER">EXPLORER</option>
          </select>
        </label>

        <label>
          Crew capacity:{' '}
          <input
            type="number"
            value={form.crewCapacity}
            onChange={e =>
              setForm({ ...form, crewCapacity: Number(e.target.value) })
            }
          />
        </label>

        <label>
          Current crew:{' '}
          <input
            type="number"
            value={form.currentCrewCount}
            onChange={e =>
              setForm({
                ...form,
                currentCrewCount: Number(e.target.value)
              })
            }
          />
        </label>

        <label>
          Home base:{' '}
          <input
            type="text"
            value={form.homeBase}
            onChange={e =>
              setForm({ ...form, homeBase: e.target.value })
            }
          />
        </label>

        <label>
          Fuel level %:{' '}
          <input
            type="number"
            step="0.1"
            value={form.fuelLevelPercent}
            onChange={e =>
              setForm({
                ...form,
                fuelLevelPercent: Number(e.target.value)
              })
            }
          />
        </label>

        <button type="submit" disabled={saving}>
          {saving
            ? 'Saving...'
            : editingId
            ? 'Update spaceship'
            : 'Add spaceship'}
        </button>
      </form>
    </div>
  );
}

export default App;
