// src/components/PersonalPage.js
import React, { useState } from 'react';
import Header from './Header';
import DataTable from './DataTable';
import './PersonalPage.css';

const data = [
  { col1: 'Row 1, Cell 1', col2: 'Row 1, Cell 2', col3: 'Row 1, Cell 3' },
  { col1: 'Row 2, Cell 1', col2: 'Row 2, Cell 2', col3: 'Row 2, Cell 3' },
  { col1: 'Row 3, Cell 1', col2: 'Row 3, Cell 2', col3: 'Row 3, Cell 3' },
];

const columns = [
  { Header: 'Column 1', accessor: 'col1' },
  { Header: 'Column 2', accessor: 'col2' },
  { Header: 'Column 3', accessor: 'col3' },
];

function PersonalPage() {
  const [userName, setUserName] = useState('Your Name');

  const handleSignOut = () => {
    console.log('Signed out');
  };

  return (
    <div className="personal-page">
      <Header userName={userName} onSignOut={handleSignOut} />
      <main className="main-content">
        <DataTable columns={columns} data={data} />
      </main>
    </div>
  );
}

export default PersonalPage;
