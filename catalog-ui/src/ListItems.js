import React, { Component } from 'react'
import PropTypes from 'prop-types'
// import escapeRegEx from 'escape-string-regexp'
// import sortBy from 'sort-by'
import { Link } from 'react-router-dom'
import ItemInfo from './ItemInfo'


class ListItems extends Component {

    static propTypes = {
      items: PropTypes.array.isRequired,
      //  onDeleteContact: PropTypes.func.isRequired,
      //   //   text: PropTypes.string.isRequired
    }

    state = {
      temp: {
        temp: true
      }
    }

    // updateShelf(shelf, name, books) {
    //   console.log('updateShelf', shelf, name, books)
    //   shelf.books = books
    //   shelf.name = name
    // }

    render() {
      console.log('ListBooks-render', this.props)
      return (
        <div className="bookshelf">
          <h2 className="bookshelf-title">Catalog</h2>
          <div className="bookshelf-books">
            <ol className="books-grid">
              {
                this.props.items.map((item, index) => (
                  <li key={index.toString()} >
                    <ItemInfo item={item} />
                  </li>
                ))
              }
            </ol>
          </div>
        </div>

    )
  }
}

export default ListItems
