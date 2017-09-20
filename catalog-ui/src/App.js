import React from 'react'
import ListItems from './ListItems'
import { Route } from 'react-router-dom'
import * as CatalogAPI from './CatalogAPI'
import './App.css'


class CatalogApp extends React.Component {
  state = {
    items : []
  }



  componentDidMount() {
    console.log('Mount')
    CatalogAPI.getAll().then((items) => {
      console.log('Mount-Items', items)
      this.setState({items})
    },
    (reason) => {
      console.log('componentDidMount', reason)
    })
  }




  render() {
    console.log('render', this.state)
    return (
      <div className="app">
        <Route exact path="/" render={() => (
          <ListItems
            items={this.state.items}
          />
        )} />
      </div>
    )
  }
}

export default CatalogApp
