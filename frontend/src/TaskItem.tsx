interface TaskItemProps {
  id: number
  description: string
}

function TaskItem({ description }: TaskItemProps) {
  return (
    <li className="task-item">
      <p>{description}</p>
    </li>
  )
}

export default TaskItem
