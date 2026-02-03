import { useState, useEffect } from 'react'
import './App.css'
import TaskItem from './TaskItem'

interface Task {
  id: number
  description: string
}

function App() {
  const [tasks, setTasks] = useState<Task[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [pageNumber, setPage] = useState(1)
  const [totalPages, setTotalPages] = useState(0)
  const pageSize = 5
  const [newTaskDescription, setNewTaskDescription] = useState('')
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    fetchTasks()
  }, [pageNumber])

  const fetchTasks = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(
        `/desafio-awer/api/tasks?pageNumber=${pageNumber}&pageSize=${pageSize}`
      )
      
      if (!response.ok) {
        throw new Error('Error al cargar tareas')
      }
      
      const data = await response.json()
      setTasks(data)
      
      const linkHeader = response.headers.get('Link')
      if (linkHeader) {
        const lastPageMatch = linkHeader.match(/pageNumber=(\d+)&pageSize=\d+>; rel="last"/)
        if (lastPageMatch) {
          setTotalPages(parseInt(lastPageMatch[1]) || 1)
        }
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error')
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!newTaskDescription.trim()) {
      return
    }
    
    setSubmitting(true)
    setError(null)
    
    try {
      const response = await fetch('/desafio-awer/api/tasks', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ description: newTaskDescription }),
      })
      
      if (!response.ok) {
        throw new Error('Error al crear tarea')
      }
      
      setNewTaskDescription('')
      fetchTasks()
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Error al crear tarea')
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="app-container">
      <h1>Lista de Tareas</h1>
      
      <form onSubmit={handleSubmit} className="task-form">
        <input
          type="text"
          value={newTaskDescription}
          onChange={(e) => setNewTaskDescription(e.target.value)}
          placeholder="Nueva tarea..."
          className="task-input"
          disabled={submitting}
        />
        <button 
          type="submit" 
          disabled={submitting || !newTaskDescription.trim()}
          className="submit-button"
        >
          {submitting ? 'Creando...' : 'Agregar'}
        </button>
      </form>
      
      {loading && <p>Cargando...</p>}
      {error && <p className="error-message">{error}</p>}
      
      <ul className="task-list">
        {tasks.map((task) => (
          <TaskItem key={task.id} id={task.id} description={task.description} />
        ))}
      </ul>
      
      <div className="pagination">
        <button onClick={() => setPage(p => p - 1)} disabled={pageNumber === 1}>
          Anterior
        </button>
        <span>Página {pageNumber} de {totalPages}</span>
        <button onClick={() => setPage(p => p + 1)} disabled={pageNumber >= totalPages}>
          Siguiente
        </button>
      </div>
    </div>
  )
}

export default App
