import React, { useState, ChangeEvent, FormEvent } from 'react'
import api from '../../services/api'
import "./styles.css"

const Home = () => {

    const [status, setStatus] = useState<String>('')

    const [formData, setFormData] = useState({
        title: '',
        body: '',
        icon: '0'
    })


    function handleInput(event: ChangeEvent<HTMLInputElement>) {
        var { name, value } = event.target

        if (name === 'icon') {
            value = event.target.checked ? '1' : '0'
        }

        console.log({name, value})

        setFormData({ ...formData, [name]: value })
    }

    function handleSubmit(event: FormEvent) {
        setStatus("Sending...")
        event.preventDefault()

        api.post('notification', { data: formData }).then(success => {
            setStatus("Notification Sended")
        }).catch(error => {
            setStatus("Error sending notification")
        })
    }

    return (
        <div className="container">
            <form action="">
                <label>Title</label>
                <input
                    value={formData.title}
                    name="title"
                    id="title"
                    onChange={handleInput}
                    type="text"
                    placeholder="title" />

                <label>Body</label>
                <input
                    value={formData.body}
                    name="body"
                    id="body"
                    onChange={handleInput}
                    type="text"
                    placeholder="body" />

                <div className="checkbox-container">
                    <input
                        className="check"
                        type="checkbox"
                        onChange={handleInput}
                        value={formData.icon}
                        name="icon"
                        id="icon" />
                    <label>Icon</label>
                </div>

                <button type="submit" onClick={handleSubmit}>Send Notification</button>
            </form>
            <h1>{status}</h1>
        </div>
    )
}

export default Home